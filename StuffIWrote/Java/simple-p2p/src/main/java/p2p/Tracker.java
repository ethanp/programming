package p2p;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Ethan Petuchowski 12/29/14
 */
public class Tracker extends Thread {

    public final static int DEFAULT_PORT = 3456;

    static ConcurrentSkipListMap<String, Swarm> swarmsByFilename;

    public static String contactInfo() {
        throw new NotImplementedException();
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

                // To make the Task to *return* something
                // you have to write this a bit differently
                pool.submit(new TrackTask(connection));
            }
            catch (IOException ex) {
                System.err.println("Exception in tracker server main listen-loop");
                System.err.println(ex.getMessage());
            }
        }

    }


    static class TrackTask implements Callable<Void> {

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
        public Void call() throws RuntimeException {
            try {
                in = Common.bufferedReader(socket);
                out = Common.printWriter(socket);

                String command = in.readLine();

                if (command == null) {
                    throw new RuntimeException("COMMAND WAS NULL");
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
                        else {
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
                            // TODO notify requesting Peer that file wasn't found
                            throw new RuntimeException("swarm not found");
                        }
                        break;
                    }
                    default:
                        throw new RuntimeException("UNKNOWN COMMAND: "+command);
                }

            }
            catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
            finally {
                try {
                    socket.close();
                }
                catch (IOException e) {
                    // ignore;
                }
            }
            return null;
        }

        P2PFileMetadata readMetadataFromSocket() {
            String filename = null;
            String base64Digest = null;
            try {
                filename = in.readLine();
                base64Digest = in.readLine();
            }
            catch (IOException e) { e.printStackTrace(); }
            byte[] rcvdDigest = DatatypeConverter.parseBase64Binary(base64Digest);


            // TODO not sure I got the InetSocketAddr part right
            return new P2PFileMetadata(filename, getInetSocketAddr(), rcvdDigest);
        }
    }
}


