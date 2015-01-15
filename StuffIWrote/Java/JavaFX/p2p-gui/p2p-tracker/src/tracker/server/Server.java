package tracker.server;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import p2p.tracker.LocalTracker;
import util.Common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Ethan Petuchowski 1/14/15
 *
 * normal server.
 * not necessarily abstract, I'm just scoping it out.
 */
public class Server extends Thread {



    protected final ObjectProperty<LocalTracker> tracker;
    protected final ObjectProperty<InetAddress> localIPAddr
            = new SimpleObjectProperty<>(Common.findMyIP());
    protected ServerSocket listener;
    protected ExecutorService pool = Executors.newFixedThreadPool(5);

    /* not to be confused with getattr */
    public InetSocketAddress getAddr() {
        return new InetSocketAddress(localIPAddr.get(), listener.getLocalPort());
    }

    public Server() {
        try {
            listener = Common.socketPortInRange(Common.PORT_MIN, Common.PORT_MAX);
        }
        catch (IOException e) {
            System.err.println(Common.ExitCodes.SERVER_FAILURE);
            System.err.println(e.getMessage());
            System.exit(Common.ExitCodes.SERVER_FAILURE.ordinal());
        }
        tracker = new SimpleObjectProperty<>(new LocalTracker(getAddr()));
    }

    @Override public void run() {
        while (true) {
            try {
                Socket conn = listener.accept();
                pool.submit(new TrackTask(conn));
            }
            catch (IOException e) {
                System.err.println("Exception in tracker server main listen-loop");
                System.err.println(e.getMessage());
            }
        }
    }

    class TrackTask extends Thread {

        protected Socket socket;
        BufferedReader in;
        PrintWriter out;

        TrackTask(Socket socket) { this.socket = socket; }

        @Override public void run() {
            try {
                in = Common.bufferedReader(socket);
                out = Common.printWriter(socket);
                String command = in.readLine();
                if (command == null) throw new RuntimeException("null command");
                System.out.println("received command: "+command);
            }
            catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

        /**
         * receive file from peer look for it among swarms
         * if it exists, add peer to swarm
         * otherwise create a new swarm for it
         */
        public void addFileRequest(){}

        /**
         * we tell a Peer who wants to download a File
         * about the specific IP Addresses of Peers in an existing Swarm
         * so it can update its internal view of the Swarm
         */
        public void sendSwarmInfo(){}

        /**
         * send full list of Swarms
         * INCLUDING specific IP Addresses of Swarm members
         */
        public void listFiles(){}
    }
}
