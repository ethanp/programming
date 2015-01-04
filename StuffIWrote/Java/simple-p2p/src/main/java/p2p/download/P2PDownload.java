package p2p.download;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import p2p.Common;
import p2p.exceptions.MetadataMismatchException;
import p2p.exceptions.P2PException;
import p2p.exceptions.SwarmNotFoundException;
import p2p.file.ChunkAvailability;
import p2p.file.P2PFile;
import p2p.file.P2PFileMetadata;
import p2p.peer.Peer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Ethan Petuchowski 12/29/14
 *
 * facilitates the transfer of a predetermined set of chunks
 * from one predetermined peer to another
 */
public class P2PDownload implements Callable<P2PFile> {

    static final Logger log = LogManager.getLogger(P2PDownload.class.getName());


    /* I'n not sure if Collection is the right thing here */
    Collection<Integer> chunkIdcs;

    /* I'm not sure if Socket is the right thing here */
    Socket from;
    Socket to;
    P2PFile pFile;
    Peer downloadingPeer;
    P2PFileMetadata fileMetadata;
    InetSocketAddress trackerAddr;

    Set<InetSocketAddress> hostPeerAddrs;


    /* we can create the sockets in here, and Sockets have a configurable timeout parameter
     * when you call
     *
     *      mySocket.connect(SocketAddress, Timeout)
     *
     */
    Timer timeoutTimer; // if connection times-out raise an Exception

    public P2PDownload(Peer downloadingPeer,
                       P2PFileMetadata fileMetadata,
                       InetSocketAddress trackerAddr)
    {
        this.downloadingPeer = downloadingPeer;
        this.fileMetadata = fileMetadata;
        this.trackerAddr = trackerAddr;
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
     * @throws Exception if unable to compute a result
     * @throws java.util.concurrent.TimeoutException if transfer times-out
     */
    @Override
    public P2PFile call() throws Exception {

        /* initialize */
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        Queue<ChunkAvailability> pq = getChunkAvailabilityQueue();
        while (!pq.isEmpty()) {
            ChunkAvailability leastAvlbChunk = pq.remove();
        }

        // start the timeoutTimer

        // open a Socket between the Client and the Host-Peer
        // pump the Chunk through it using an Object-Stream

        // if the Timer runs out, throw a TimeoutException

        throw new NotImplementedException();
    }

    Queue<ChunkAvailability> getChunkAvailabilityQueue() throws P2PException {
        int chunkCt = fileMetadata.getNumChunks();
        Set<InetSocketAddress> seeders = getSeedersForFile(fileMetadata, trackerAddr);
        List<ChunkAvailability> chunkAvlbtyCts = ChunkAvailability.createList(chunkCt);
        for (InetSocketAddress seederAddr : seeders) {
            BitSet chunkBools = requestChunkListing(seederAddr);
            addCts(chunkBools, chunkAvlbtyCts, seederAddr);
        }
        return new PriorityQueue<>(chunkAvlbtyCts);
    }

    BitSet requestChunkListing(InetSocketAddress seederAddr) {
        return null;
    }

    void addCts(BitSet bitSet, List<ChunkAvailability> ctArr, InetSocketAddress addr) {
        for (int i = 0; i < ctArr.size(); i++)
            if (bitSet.get(i))
                ctArr.get(i).addOwner(addr);
    }

}
