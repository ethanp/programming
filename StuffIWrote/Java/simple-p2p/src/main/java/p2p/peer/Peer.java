package p2p.peer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import p2p.Common;
import p2p.console.P2PConsole;
import p2p.console.PeerConsole;
import p2p.download.P2PDownload;
import p2p.exceptions.MetadataMismatchException;
import p2p.exceptions.SwarmNotFoundException;
import p2p.file.P2PFile;
import p2p.file.P2PFileMetadata;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.FileSystemException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Ethan Petuchowski 12/29/14
 * <p/>
 * Peers act as Clients for leeching Servers for seeding
 */
public class Peer {

    static final Logger log = LogManager.getLogger(Peer.class.getName());

    /** FIELDS * */

    public InetAddress externalIPAddr = Common.findMyIP();
    Path localDir;

    public InetSocketAddress trkAddr;

    // LowPriorityTODO: ongoingTransfers
    Set<P2PDownload> ongoingTransfers = new ConcurrentSkipListSet<>();

    Set<P2PFile> completeAndSeeding = new ConcurrentSkipListSet<>();

    public PeerListener listenerThread;

    P2PConsole console;

    /** CONSTRUCTORS **/

    Peer(String dirString) {
        log.info("creating peer");
        localDir = Paths.get(dirString);
        listenerThread = new PeerListener(this);
        new Thread(listenerThread).start();
    }

    public Peer() {
        this(".");
    }

    public Peer(P2PConsole console) {
        this();
        this.console = console;
    }

    public Peer(InetSocketAddress trackerAddr) {
        this();
        this.trkAddr = trackerAddr;
    }


    /** GETTERS & SETTERS **/

    public InetSocketAddress getAddr() {
        return new InetSocketAddress(externalIPAddr, getListeningPort());
    }


    public Peer setTracker(InetSocketAddress trackerAddr) {
        this.trkAddr = trackerAddr;
        return this;
    }

    public int getListeningPort() {
        return listenerThread.listeningPort;
    }

    public P2PFile getSeedingFile(P2PFileMetadata meta) throws FileNotFoundException {
        // LowPriorityTODO to be more efficient, completeAndSeeding should be a Map<P2PMetadata,P2PFile>
        for (P2PFile pFile : completeAndSeeding)
            if (pFile.metadata.equals(meta))
                return pFile;
        throw new FileNotFoundException("metadata didn't match any known files");
    }

    /** PEER-TO-PEER METHODS **/

    /**
     * Inform saved Tracker that this Peer is seeding the file with the given name.
     * A P2PFile instance will be created and saved at the Peer from the filename.
     * The P2PFile's Metadata instance will be transmitted to the Tracker.
     * @param pathString location of file to share
     * @return the server's Common.StatusCodes response
     */
    public Common.StatusCodes shareFile(String pathString)
            throws FileNotFoundException, FileSystemException
    {
        P2PFile sharedFile = new P2PFile(pathString, trkAddr);
        completeAndSeeding.add(sharedFile);
        Common.StatusCodes status = informTrackerAboutFile(sharedFile);
        return status;
    }

    /**
     * list "filename numSeeders" for each file of this peer's default tracker
     */
    public List<P2PFileMetadata> listSavedTracker() throws ConnectException {
        return listTracker(trkAddr);
    }

    /**
     * list "filename numSeeders" for each file of the tracker at the given address
     */
    public List<P2PFileMetadata> listTracker(InetSocketAddress trackerAddr) throws ConnectException {
        List<P2PFileMetadata> theListing = new ArrayList<>();
        try (Socket trkr = new Socket(trackerAddr.getAddress(), trackerAddr.getPort())) {
            PrintWriter out = Common.printWriter(trkr);

            /* we *must* create an OOS *before* an OIS
             * stackoverflow.com/questions/5658089 */
            out.println(Common.LIST_FILES_CMD);
            ObjectOutputStream objOut = Common.objectOStream(trkr);
            ObjectInputStream objIn = Common.objectIStream(trkr);

            int numSwarms = objIn.readInt();

            /* we *must* catch EOFException for flow-control on readObject()
             * stackoverflow.com/questions/12684072 */
            for (int i = 0; i < numSwarms; i++) {
                P2PFileMetadata metadata = (P2PFileMetadata) objIn.readObject();
                log.debug(objIn.available());
                int numPeers = objIn.readInt();

                log.printf(Level.INFO,
                           "List File Item: %s; %d peer(s)\n",
                           metadata.getFilename(), numPeers);

                theListing.add(metadata);
            }
        }
        catch (EOFException e) {
            log.info("done receiving file list");
        }
        catch (ConnectException e) { throw e; }
        catch(IOException | ClassNotFoundException e){ e.printStackTrace(); }
            return theListing;
        }

    /**
     * Download P2PFile from this Peer's saved Tracker.
     * @param fileMetadata a metadata object corresponding to the file to request
     * @return the P2PFile corresponding to filename
     */
    public P2PFile downloadFromSavedTracker(P2PFileMetadata fileMetadata)
            throws InterruptedException, ExecutionException,
                   TimeoutException, SwarmNotFoundException,
                   MetadataMismatchException
    {
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
                            InetSocketAddress trackerAddr)
            throws InterruptedException, ExecutionException,
                   TimeoutException, SwarmNotFoundException,
                   MetadataMismatchException
    {
        P2PFile downloadedFile =
                new P2PDownload(this, fileMetadata, trackerAddr).call();
        return downloadedFile;
    }


    /** NON-INTERFACE METHODS **/

    private Common.StatusCodes informTrackerAboutFile(P2PFile file2Share) {
        try (Socket trkr = Common.socketAtAddr(trkAddr)) {
            PrintWriter out = Common.printWriter(trkr);
            out.println(Common.ADD_FILE_CMD);
            ObjectOutputStream oos = Common.objectOStream(trkr);
            ObjectInputStream ois = Common.objectIStream(trkr);
            oos.writeObject(new InetSocketAddress(externalIPAddr,
                                                  listenerThread.listeningPort));
            oos.writeObject(file2Share.metadata);
            Common.StatusCodes status = (Common.StatusCodes) ois.readObject();
            return status;
        }
        catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
        return null;
    }

    void startPeerConsole() {
        if (console != null) {
            Common.putIPAddr(externalIPAddr, getListeningPort());
            new PeerConsole(this).start();
        }
    }
}

