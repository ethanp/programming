package p2p;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Ethan Petuchowski 12/29/14
 *
 * Peers act as
 *      Clients for leeching
 *      Servers for seeding
 */
public class Peer {

    static int DEFAULT_LISTEN_PORT = 2102;
    InetAddress ipAddr;
    InetAddress knownTracker;
    Path localDir;

    Set<P2PTransfer> ongoingTransfers = new ConcurrentSkipListSet<>();
    Set<P2PFile> completeAndSeeding = new ConcurrentSkipListSet<>();


    /** PUBLIC INTERFACE **/

    /**
     * @param pathString location of file to share
     */
    public void shareFile(String pathString) {
        P2PFile sharedFile = new P2PFile(pathString);
        completeAndSeeding.add(sharedFile);
        informTrackerAboutFile(sharedFile);
    }

    public void setTracker(InetAddress trackerAddr) {
        knownTracker = trackerAddr;
    }

    void informTrackerAboutFile(P2PFile file2Share) {
        Socket toTracker = null;
        try { toTracker = new Socket(knownTracker, Tracker.DEFAULT_PORT); }
        catch (IOException e) { e.printStackTrace(); }
        if (toTracker == null) { throw new RuntimeException("null tracker socket"); }
        PrintWriter out = Common.printWriter(toTracker);
        out.println(Common.ADD_FILE_CMD);
        out.println(file2Share.filenameString());
        out.println(file2Share.base64Digest());
    }

    Peer(String dirString) {
        localDir = Paths.get(dirString);
        findMyIP();
    }

    void findMyIP() {
        URL aws = null;
        try { aws = new URL("http://checkip.amazonaws.com"); }
        catch (MalformedURLException e) { e.printStackTrace(); }
        BufferedReader in = null;
        try {
            if (aws != null) {
                in = new BufferedReader(new InputStreamReader(aws.openStream()));
                String ip = in.readLine();
                ipAddr = InetAddress.getByName(ip);
            }
        }
        catch (IOException e) { e.printStackTrace(); }
        finally {
            if (in != null) {
                try { in.close(); }
                catch (IOException e) { e.printStackTrace(); }
            }
        }
    }

    static class PeerListener {
        P2PTransfer xfer;
        int myListenPort;
        ServerSocket socket;
        BufferedReader in;
        BufferedWriter out;

        public PeerListener(P2PTransfer xfer) {
            this.xfer = xfer;

            try {
                this.socket = new ServerSocket(DEFAULT_LISTEN_PORT);
            }
            catch (IOException e) { e.printStackTrace(); }
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            }
            catch (IOException e) { e.printStackTrace(); }
        }

        @Override
        public void run() {

            // TODO no this is not supposed to just be an echo server

            while (true) {
                try {
                    String msg = "Received: " + in.readLine();
                    System.out.println(msg);
                    out.write(msg);
                    out.newLine();
                    out.flush();
                }
                catch (IOException e) { e.printStackTrace(); }
            }
        }
    }
}
