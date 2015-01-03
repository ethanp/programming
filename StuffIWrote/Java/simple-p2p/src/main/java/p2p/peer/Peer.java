package p2p.peer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import p2p.Common;
import p2p.P2PTransfer;
import p2p.exceptions.MetadataMismatchException;
import p2p.exceptions.P2PException;
import p2p.exceptions.SwarmNotFoundException;
import p2p.file.ChunkAvailability;
import p2p.file.P2PFile;
import p2p.file.P2PFileMetadata;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.FileSystemException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Ethan Petuchowski 12/29/14
 * <p/>
 * Peers act as Clients for leeching Servers for seeding
 */
public class Peer {

    static final Logger log = LogManager.getLogger(Peer.class.getName());

    /** FIELDS * */

    InetAddress externalIPAddr = Common.findMyIP();
    Path localDir;

    InetSocketAddress trkAddr;

    Set<P2PTransfer> ongoingTransfers = new ConcurrentSkipListSet<>();
    Set<P2PFile> completeAndSeeding = new ConcurrentSkipListSet<>();

    PeerListener listenerThread;

    /** CONSTRUCTORS * */

    Peer(String dirString) {
        log.info("creating peer");
        localDir = Paths.get(dirString);
        listenerThread = new PeerListener();
        new Thread(listenerThread).start();
    }

    Peer() {
        this(".");
    }


    /** PUBLIC INTERFACE **/

    /**
     * @param pathString location of file to share
     */
    public void shareFile(String pathString) throws FileNotFoundException, FileSystemException {
        P2PFile sharedFile = new P2PFile(pathString, trkAddr);
        completeAndSeeding.add(sharedFile);
        informTrackerAboutFile(sharedFile);
    }

    public void setTracker(InetSocketAddress trackerAddr) {
        this.trkAddr = trackerAddr;
    }

    public int getListeningPort() {
        return listenerThread.listeningPort;
    }

    /**
     * list "filename numSeeders" for each file of this peer's default tracker
     */
    public SortedSet<P2PFileMetadata> listSavedTracker() {
        return listTracker(trkAddr);
    }

    /**
     * list "filename numSeeders" for each file of the tracker at the given address
     */
    public SortedSet<P2PFileMetadata> listTracker(InetSocketAddress trackerAddr) {
        SortedSet<P2PFileMetadata> theListing = new TreeSet<>();
        try (Socket trkr = new Socket(trackerAddr.getAddress(), trkAddr.getPort())) {
            PrintWriter out = Common.printWriter(trkr);

            /* we *must* create an OOS *before* an OIS
             * stackoverflow.com/questions/5658089 */
            out.println(Common.LIST_FILES_CMD);
            ObjectOutputStream objOut = new ObjectOutputStream(trkr.getOutputStream());
            objOut.flush();
            ObjectInputStream objIn = new ObjectInputStream(trkr.getInputStream());

            P2PFileMetadata metadata;
            int numPeers;
            /* we *must* catch EOFException for flow-control on readObject()
             * stackoverflow.com/questions/12684072 */
            while (true) {
                metadata = (P2PFileMetadata) objIn.readObject();
                log.debug(objIn.available());
                numPeers = objIn.readInt();

                // LowPriorityTODO: should I do something with the numPeers var?

                log.printf(Level.INFO,
                           "List File Item: %s; %d peer(s)\n",
                           metadata.getFilename(), numPeers);

                theListing.add(metadata);
            }
        }
        catch (EOFException e) {
            log.info("done receiving file list");
        }
        catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
        return theListing;
    }

    /**
     * Download P2PFile from this Peer's saved Tracker.
     * @param fileMetadata a metadata object corresponding to the file to request
     * @return the P2PFile corresponding to filename
     */
    public P2PFile downloadFromSavedTracker(P2PFileMetadata fileMetadata)
            throws P2PException {
        return download(fileMetadata, trkAddr);
    }

    /**
     * Maybe at the some point we could put a DownloadPolicy via Dependency
     * Injection
     * @param fileMetadata a metadata object corresponding to the file to request
     * @param trackerAddr  the IPAddr:Port of the Tracker from which to download
     * @return the P2PFile corresponding to "filename"
     */
    public P2PFile download(P2PFileMetadata fileMetadata,
                            InetSocketAddress trackerAddr) throws P2PException {
        int chunkCt = fileMetadata.getNumChunks();
        Set<InetSocketAddress> seeders = getSeedersForFile(fileMetadata, trackerAddr);
        List<ChunkAvailability> chunkAvlbtyCts = ChunkAvailability.createList(chunkCt);

        for (InetSocketAddress seederAddr : seeders) {
            BitSet hostedChunks = requestChunkListing(seederAddr);
            addCts(hostedChunks, chunkAvlbtyCts, seederAddr);
        }

        Queue<ChunkAvailability> pq = new PriorityQueue<>(chunkAvlbtyCts);

        // TODO use a ThreadPool to submit P2PTransfer Tasks 10 at a time?

        while (!pq.isEmpty()) {

        }

        return null;
    }


    /** PRIVATE METHODS * */

    void addCts(BitSet bitSet, List<ChunkAvailability> ctArr, InetSocketAddress addr) {
        for (int i = 0; i < ctArr.size(); i++)
            if (bitSet.get(i))
                ctArr.get(i).addOwner(addr);
    }

    BitSet requestChunkListing(InetSocketAddress seederAddr) {
        return null;
    }

    // package-local so it can be tested
    Set<InetSocketAddress> getSeedersForFile(
            P2PFileMetadata fileMetadata, InetSocketAddress trackerAddr)
            throws P2PException {
        Set<InetSocketAddress> toRet = null;
        try (Socket trkrS = Common.socketAtAddr(trackerAddr)) {
            PrintWriter writer = Common.printWriter(trkrS);
            writer.println(Common.GET_SEEDERS_CMD);
            ObjectOutputStream oos = Common.objectOStream(trkrS);
            ObjectInputStream ois = Common.objectIStream(trkrS);
            oos.writeObject(fileMetadata);
            Object obj = ois.readObject();
            if (obj instanceof Common.StatusCodes) {
                Common.StatusCodes status = (Common.StatusCodes) obj;
                if (status.equals(Common.StatusCodes.SWARM_NOT_FOUND)) {
                    throw new SwarmNotFoundException();
                }
                if (status.equals(Common.StatusCodes.METADATA_MISMATCH)) {
                    throw new MetadataMismatchException();
                }
                else {
                    Common.StatusCodes code = (Common.StatusCodes) obj;
                    throw new RuntimeException("unknown status code: "+code.toString());
                }
            }
            toRet = (Set<InetSocketAddress>) obj;

        }
        catch (ClassNotFoundException | IOException e) { e.printStackTrace(); }
        return toRet;
    }

    private void informTrackerAboutFile(P2PFile file2Share) {
        try (Socket trkr = Common.socketAtAddr(trkAddr)) {
            PrintWriter out = Common.printWriter(trkr);
            out.println(Common.ADD_FILE_CMD);
            ObjectOutputStream oos = Common.objectOStream(trkr);
            ObjectInputStream ois = Common.objectIStream(trkr);
            oos.writeObject(new InetSocketAddress(externalIPAddr,
                                                  listenerThread.listeningPort));
            oos.writeObject(file2Share.metadata);
        }
        catch (IOException e) { e.printStackTrace(); }
    }
}

