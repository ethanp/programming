package p2p;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Ethan Petuchowski 12/29/14
 * <p/>
 * based on the PooledDaytimeServer in "Java Network Programming"
 */
public class Tracker {

    public final static int DEFAULT_PORT = 3456;

    static ConcurrentSkipListMap<String, Swarm> swarmsByFilename;

    public static String contactInfo() {
        throw new NotImplementedException();
    }

    public static void main(String[] args) {
        System.out.println("Starting tracker/index server"); // TODO create a Logger
        ExecutorService pool = Executors.newFixedThreadPool(5);
        try (ServerSocket listener = new ServerSocket(PORT)) {
            while (true) {
                try {
                    Socket connection = listener.accept();

                    // To make the Task to *return* something
                    // you have to write this a bit differently
                    pool.submit(new TrackTask(connection));
                }
                catch (IOException ex) {
                    System.out.println("Exception in tracker server main listen-loop");
                    System.out.println(ex.getMessage());
                }
            }
        }
        catch (IOException ex) {
            System.err.println("Couldn't start server");
            System.out.println(ex.getMessage());
        }
    }

    static class TrackTask implements Callable<Void> {

        private Socket socket;
        BufferedReader in;
        PrintWriter out;

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
                        String filename = in.readLine();
                        String base64Digest = in.readLine();

                        byte[] rcvdDigest = DatatypeConverter.parseBase64Binary(base64Digest);
                        SocketAddress addr = socket.getRemoteSocketAddress();
                        if (swarmsByFilename.containsKey(filename)) {
                            Swarm swarm = swarmsByFilename.get(filename);
                            if (Arrays.equals(swarm.pFileMetadata.sha256Digest, rcvdDigest)) {
                                swarm.
                            }
                            else {
                                throw new SecurityException("given hash didn't match");
                            }
                        }
                        else {
                            Swarm swarm = new Swarm(addr, filename)
                        }

                        break;
                    }
                    case "locate": {
                        String filename = in.readLine();
                        String base64Digest = in.readLine();
                        byte[] rcvdDigest = DatatypeConverter.parseBase64Binary(base64Digest);
                        if (swarmsByFilename.containsKey(filename)) {
                            Swarm swarm = swarmsByFilename.get(filename);
                            if (Arrays.equals(swarm.file.sha256Digest, rcvdDigest)) {
                                // TODO send the swarm.seedersByChunk (somehow...)
                            }
                            else {
                                throw new SecurityException("given hash didn't match");
                            }
                        }
                        break;
                    }
                    default:
                        throw new RuntimeException("UNKNOWN COMMAND: "+command);
                }

            }
            catch (IOException ex) {
                System.err.println(ex);
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

        List<Peer> getSeedersForFile(P2PFile p2PFile) {
            throw new NotImplementedException();
        }

        List<P2PFile> listKnownFiles() {
            throw new NotImplementedException();
        }

        boolean isTrackingFile(String filename) {
            throw new NotImplementedException();
        }
    }
}


