package p2p;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Ethan Petuchowski 12/29/14
 *
 * Peers act as
 *      Clients for leeching
 *      Servers for seeding
 */
public class Peer {

    static final Logger log = LogManager.getLogger(Peer.class.getName());

    /** FIELDS * */

    InetAddress ipAddr = Common.findMyIP();
    Path localDir;

    InetSocketAddress trkAddr;

    Set<P2PTransfer> ongoingTransfers = new ConcurrentSkipListSet<>();
    Set<P2PFile> completeAndSeeding = new ConcurrentSkipListSet<>();


    /** CONSTRUCTORS **/

    Peer(String dirString) {
        log.info("creating peer");
        localDir = Paths.get(dirString);
    }

    Peer() {
        this(".");
    }


    /** PUBLIC INTERFACE **/

    /**
     * @param pathString location of file to share
     */
    public void shareFile(String pathString) {
        P2PFile sharedFile = new P2PFile(pathString, trkAddr);
        completeAndSeeding.add(sharedFile);
        informTrackerAboutFile(sharedFile);
    }

    public void setTracker(InetSocketAddress trackerAddr) {
        this.trkAddr = trackerAddr;
    }

    /**
     * list "filename numSeeders" for each file of this peer's default tracker
     */
    public SortedSet<String> listTracker() {
        return listTracker(trkAddr);
    }

    /**
     * list "filename numSeeders" for each file of the tracker at the given address
     */
    public SortedSet<String> listTracker(InetSocketAddress trackerAddr) {
        SortedSet<String> theListing = new TreeSet<>();
        try (Socket trkr = new Socket(trackerAddr.getAddress(), trkAddr.getPort())) {
            PrintWriter out = Common.printWriter(trkr);
            BufferedReader in = Common.bufferedReader(trkr);
            out.println(Common.LIST_FILES_CMD);
            out.close();

            // I thinks basically this will keep reading until
            // the connection is closed BY THE SERVER
            String line;
            while ((line = in.readLine()) != null) {
                log.info("Gettin jiggy wit "+line);
                theListing.add(line);
            }
        }
        catch (IOException e) { e.printStackTrace(); }
        return theListing;
    }


    /** PRIVATE METHODS **/

    private void informTrackerAboutFile(P2PFile file2Share) {
        try (Socket trkr = new Socket(trkAddr.getAddress(), trkAddr.getPort())) {
            PrintWriter out = Common.printWriter(trkr);
            out.println(Common.ADD_FILE_CMD);
            out.println(file2Share.filenameString());
            out.println(file2Share.base64Digest());
        }
        catch (UnknownHostException e) {
            log.error("Peer ");
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    static class PeerListener extends Thread {

        @Override
        public void run() {
            ExecutorService threadPool = Executors.newFixedThreadPool(50);

            // "0" finds a free port: stackoverflow.com/questions/2675362
            // and I was using that, but I just set my router to forward 3-3.5K to me
            try (ServerSocket listener = new ServerSocket(3242)) {
                while (true) {
                    Socket conn = listener.accept();
                    threadPool.submit(new PeerServeTask(conn));
                }
            }
            catch (IOException e) {
                log.error("Peer not connected to Internet: can't contact tracker");
                e.printStackTrace();
                System.exit(Common.StatusCodes.NO_INTERNET.ordinal());
            }
        }

        static class PeerServeTask extends Thread {

            P2PTransfer xfer;
            Socket socket;
            BufferedReader in;
            BufferedWriter out;

            PeerServeTask(Socket socket) {
                this.socket = socket;
                in = Common.bufferedReader(socket);
                out = Common.bufferedWriter(socket);
            }

            @Override
            public void run() {

                // TODO no this is not supposed to just be an echo server

                while (true) {
                    try {
                        String msg = "Received: "+in.readLine();
                        log.info(msg);
                        out.write(msg);
                        out.newLine();
                        out.flush();
                    }
                    catch (IOException e) { e.printStackTrace(); }
                    finally { try { socket.close(); } catch (IOException e) {} }
                }
            }
        }
    }
}
