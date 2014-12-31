package p2p;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Ethan Petuchowski 12/29/14
 */
public class Tracker extends Thread {

    ConcurrentSkipListMap<String, Swarm> swarmsByFilename
            = new ConcurrentSkipListMap<>();

    public boolean isTrackingFilename(String filename) {
        return swarmsByFilename.containsKey(filename);
    }

    ServerSocket listener;

    public InetSocketAddress getInetSocketAddr() {

        InetAddress ia   = listener.getInetAddress();
        int         port = listener.getLocalPort();

        return new InetSocketAddress(ia, port);
    }

    ExecutorService pool = Executors.newFixedThreadPool(5);

    public Tracker() {
        try { listener = new ServerSocket(0); }
        catch (IOException ex) {
            System.err.println("Couldn't start server");
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("Starting tracker/index server"); // TODO create a Logger
        while (true) {
            try {
                Socket connection = listener.accept();
                pool.submit(new TrackTask(connection));
            }
            catch (IOException ex) {
                System.err.println("Exception in tracker server main listen-loop");
                System.err.println(ex.getMessage());
            }
        }
    }


    class TrackTask extends Thread {

        private Socket socket;
        BufferedReader in;
        PrintWriter out;

        public InetSocketAddress getInetSocketAddr() {
            InetAddress ia   = socket.getInetAddress();
            int         port = socket.getLocalPort();
            return new InetSocketAddress(ia, port);
        }

        TrackTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() throws RuntimeException {
            try {
                in = Common.bufferedReader(socket);
                out = Common.printWriter(socket);

                String command = in.readLine();

                if (command == null) {
                    throw new RuntimeException("null command");
                }

                switch (command) {
                    case Common.ADD_FILE_CMD: {
                        P2PFileMetadata rcvdMeta = readMetadataFromSocket();
                        SocketAddress addr = socket.getRemoteSocketAddress();
                        String filename = rcvdMeta.filename;
                        System.out.println("received add file cmd for "+filename);
                        if (swarmsByFilename.containsKey(filename)) {
                            Swarm swarm = swarmsByFilename.get(filename);
                            if (swarm.pFileMetadata.equals(rcvdMeta)) {
                                swarm.addSeeder(addr);
                            }
                            else {
                                throw new SecurityException("metadata didn't match");
                            }
                        }
                        else { /* no existing swarm for this filename */
                            swarmsByFilename.put(filename, new Swarm(addr, rcvdMeta));
                        }
                        break;
                    }
                    case "locate": {
                        P2PFileMetadata rcvdMeta = readMetadataFromSocket();
                        SocketAddress addr = socket.getRemoteSocketAddress();
                        String filename = rcvdMeta.filename;
                        if (swarmsByFilename.containsKey(filename)) {
                            Swarm swarm = swarmsByFilename.get(filename);
                            if (swarm.pFileMetadata.equals(rcvdMeta)) {
                                // TODO send the swarm.seedersByChunk (somehow...)
                            }
                            else {
                                throw new SecurityException("hash didn't match");
                            }
                        }
                        else {
                            out.println("swarm not found");
                        }
                        break;
                    }
                    default:
                        throw new RuntimeException("UNKNOWN COMMAND: "+command);
                }

            }
            catch (IOException ex) { System.err.println(ex.getMessage()); }
            finally { try { socket.close(); } catch (IOException e) {/*ignore*/} }
        }

        P2PFileMetadata readMetadataFromSocket() {
            // can't use try-with-resources with String
            // because it doesn't implement `AutoCloseable`
            String filename = null;
            String base64Digest = null;
            try {
                filename = in.readLine();
                base64Digest = in.readLine();
            }
            catch (IOException e) { e.printStackTrace(); }
            byte[] rcvdDigest = DatatypeConverter.parseBase64Binary(base64Digest);
            return new P2PFileMetadata(filename, getInetSocketAddr(), rcvdDigest);
        }
    }
}


