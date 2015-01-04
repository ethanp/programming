package p2p.download;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import p2p.Common;
import p2p.exceptions.MetadataMismatchException;
import p2p.exceptions.P2PException;
import p2p.exceptions.SwarmNotFoundException;
import p2p.file.Chunk;
import p2p.file.ChunkAvailability;
import p2p.file.P2PFile;
import p2p.file.P2PFileMetadata;
import p2p.peer.Peer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

/**
 * Ethan Petuchowski 12/29/14
 *
 * facilitates the transfer of a predetermined set of chunks
 * from one predetermined peer to another
 */
public class P2PDownload implements Callable<P2PFile> {

    static final Logger log = LogManager.getLogger(P2PDownload.class.getName());

    /* FIELDS */
    Peer downloadingPeer;
    P2PFileMetadata meta;
    InetSocketAddress trackerAddr;
    ExecutorService threadPool = Executors.newFixedThreadPool(30);
    BitSet chunksComplete;
    int numChunks;

    public P2PDownload(Peer downloadingPeer,
                       P2PFileMetadata fileMetadata,
                       InetSocketAddress trackerAddr)
    {
        this.downloadingPeer = downloadingPeer;
        this.meta = fileMetadata;
        this.trackerAddr = trackerAddr;
        this.numChunks = meta.getNumChunks();
        this.chunksComplete = new BitSet(numChunks);
    }

    Set<InetSocketAddress> getSeedersForFile
    (P2PFileMetadata fileMetadata, InetSocketAddress trackerAddr)
            throws P2PException
    {
        Set<InetSocketAddress> hostAddrs = null;

        /* connect to tracker */
        try (Socket trkrS = Common.socketAtAddr(trackerAddr)) {

            /* send "get seeders" command to tracker */
            PrintWriter writer = Common.printWriter(trkrS);
            writer.println(Common.GET_SEEDERS_CMD);

            /* send metadata of file to get seeders of */
            ObjectOutputStream oos = Common.objectOStream(trkrS);
            ObjectInputStream ois = Common.objectIStream(trkrS);
            oos.writeObject(fileMetadata);

            /* retrieve response */
            Object obj = ois.readObject();

            /* if request was faulty, throw appropriate error */
            if (obj instanceof Common.StatusCodes) {
                Common.StatusCodes status = (Common.StatusCodes) obj;
                if (status.equals(Common.StatusCodes.SWARM_NOT_FOUND))
                    throw new SwarmNotFoundException();
                if (status.equals(Common.StatusCodes.METADATA_MISMATCH))
                    throw new MetadataMismatchException();
                else {
                    Common.StatusCodes code = (Common.StatusCodes) obj;
                    throw new RuntimeException(
                            "unknown status code: "
                            + code.ordinal() + " " + code.toString());
                }
            }

            /* if request was successful, save the retrieved list of addresses */
            hostAddrs = (Set<InetSocketAddress>) obj;

        }
        catch (ClassNotFoundException | IOException e) { e.printStackTrace(); }

        return hostAddrs;
    }

    /**
     * Downloads a P2PFile or throws an Exception if unable to do so.
     * @return downloaded P2PFile
     * @throws Exception                             if unable to compute a result
     * @throws java.util.concurrent.TimeoutException if transfer times-out
     */
    @Override
    public P2PFile call() throws Exception {

        List<Chunk> ofFile = new ArrayList<>();

        /* really no good reason to do it this way... */
        for (int i = 0; i < 10; i++) {
            List<Chunk> done = downloadFor(3/*seconds*/);
            ofFile.addAll(done);
            for (Chunk c : done) chunksComplete.set(c.idx, true);
            if (ofFile.size() == numChunks) break;
        }

        if (ofFile.size() != numChunks)
            throw new TimeoutException();

        return new P2PFile(meta, ofFile);
    }

    /**
     * download chunks for specified duration then cancel any pending transfers
     * Surely this is an AWFUL way of going about this, but for now I just want
     * to transfer a file, THEN I'll think about how BEST to do that.
     *
     * @param seconds how long to download chunks for before cancelling
     * @return a List of finished Chunks
     */
    List<Chunk> downloadFor(int seconds)
            throws P2PException, ExecutionException, InterruptedException
    {
        /* figure out what chunks to get and how to prioritize them */
        Queue<ChunkAvailability> pq = getChunkAvailabilityQueue();

        /* start downloading the chunks */
        List<Future<Chunk>> futureChunks = new ArrayList<>();
        while (!pq.isEmpty()) {
            ChunkAvailability ck = pq.remove();
            Callable<Chunk> dl = new ChunkDownload(meta, ck.chunkIdx, ck.ownerAddrs);
            Future<Chunk> futChunk = threadPool.submit(dl);
            futureChunks.add(futChunk);
        }

        /* check after 3 seconds for finished chunks
         *
         * NOTE: there's really no good reason to do it this way
         * except that it may help in debugging across multiple machines
         */
        Thread.sleep(seconds*1000); // milliseconds
        List<Chunk> completeChunks = new ArrayList<>();
        int numRcvd = chunksComplete.cardinality();
        for (Future<Chunk> chunkFuture : futureChunks) {
            if (chunkFuture.isDone()) {
                Chunk doneChunk = chunkFuture.get();
                completeChunks.add(doneChunk);

                log.printf(Level.INFO,
                        "Chunk idx %d of \"%s\" received. %d of %d chunks so far.",
                        doneChunk.idx, meta.getFilename(), ++numRcvd, numChunks);
            }
        }

        /* cancel any transfers that haven't finished */
        for (Future<Chunk> chunkFuture : futureChunks)
            chunkFuture.cancel(true); // doesn't affect completed tasks

        return completeChunks;
    }

    Queue<ChunkAvailability> getChunkAvailabilityQueue() throws P2PException {
        List<ChunkAvailability> chunkAvlbtyCts =
                ChunkAvailability.createList(chunksComplete, numChunks);

        Set<InetSocketAddress> seeders = getSeedersForFile(meta, trackerAddr);
        for (InetSocketAddress seederAddr : seeders) {
            BitSet chunkBools = requestChunkListing(seederAddr);
            addCts(chunkBools, chunkAvlbtyCts, seederAddr);
        }
        return new PriorityQueue<>(chunkAvlbtyCts);
    }

    /**
     * this will be much more useful when it is NOT the case that every owner
     * has the ENTIRE file
     */
    BitSet requestChunkListing(InetSocketAddress seederAddr) {
        try (Socket socket = Common.socketAtAddr(seederAddr)) {
            ObjectOutputStream oos = Common.objectOStream(socket);
            ObjectInputStream ois = Common.objectIStream(socket);
            oos.writeObject(Common.CHUNK_BITSET_CMD);
            oos.writeObject(meta);
            Object response = ois.readObject();
            if (response instanceof Common.StatusCodes) {
                Common.StatusCodes code = (Common.StatusCodes) response;
                if (code.equals(Common.StatusCodes.FILE_NOT_FOUND))
                    throw new FileNotFoundException("requested "+meta.getFilename());
                else throw new RuntimeException("Unknown status code "+code);
            }
            return (BitSet) response;
        }
        catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
        return null;
    }

    void addCts(BitSet bitSet, List<ChunkAvailability> ctArr, InetSocketAddress addr) {
        for (int i = 0; i < ctArr.size(); i++)
            if (bitSet.get(i))
                ctArr.get(i).addOwner(addr);
    }
}
