package p2p;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
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

    /** FIELDS * */

    InetAddress ipAddr = Common.findMyIP();
    Path localDir;

    InetSocketAddress trkAddr;

    Set<P2PTransfer> ongoingTransfers = new ConcurrentSkipListSet<>();
    Set<P2PFile> completeAndSeeding = new ConcurrentSkipListSet<>();


    /** CONSTRUCTORS **/

    Peer(String dirString) {
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

    private void informTrackerAboutFile(P2PFile file2Share) {
        try (Socket trkr = new Socket(trkAddr.getAddress(), trkAddr.getPort())) {
            PrintWriter out = Common.printWriter(trkr);
            out.println(Common.ADD_FILE_CMD);
            out.println(file2Share.filenameString());
            out.println(file2Share.base64Digest());
        }
        catch (UnknownHostException e) {
            System.err.println("Peer ");
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
                System.err.println("Peer not connected to Internet: can't contact tracker");
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
                        System.out.println(msg);
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
