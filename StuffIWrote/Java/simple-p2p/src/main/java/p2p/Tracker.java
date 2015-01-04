package p2p;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import p2p.file.P2PFileMetadata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Ethan Petuchowski 12/29/14
 */
public class Tracker extends Thread {

    /* LOGGER */

    static final Logger log = LogManager.getLogger(Tracker.class.getName());

    /* FIELDS */

    /*
     * allocation parameters suggested here:
     *   ria101.wordpress.com/2011/12/12/concurrenthashmap-avoid-a-common-misuse/
     *
     * The first one        is for how big you think it'll get
     * The second           is for how often it'll grow?? Not really sure.
     * The third/last one   is for how many concurrent writers you think you'll have
     *
     * So we are limited to a SINGLE Swarm per Filename, which is probably a GOOD thing
     *
     */
    public final ConcurrentHashMap<String, Swarm> swarmsByFilename
            = new ConcurrentHashMap<>(8, 0.9f, 1);

    InetAddress localIPAddr = Common.findMyIP();
    ServerSocket listener;
    ExecutorService pool = Executors.newFixedThreadPool(5);
    P2PConsole console;

    /* CONSTRUCTORS */

    public Tracker() {
        try { listener = Common.socketPortInRange(Common.PORT_MIN, Common.PORT_MAX); }
        catch (IOException ex) {
            log.error("Couldn't start server");
            log.error(ex.getMessage());
        }
    }

    public Tracker(P2PConsole console) {
        this();
        this.console = console;
        console.putIPAddr(localIPAddr, listener.getLocalPort());
    }

    /* METHODS */

    public boolean isTrackingFilename(String filename) {
        return swarmsByFilename.containsKey(filename);
    }


    public InetSocketAddress getInetSocketAddr() {
        return new InetSocketAddress(localIPAddr, listener.getLocalPort());
    }

    /* RUN */

    @Override
    public void run() {
        log.info("Starting tracker/index server");
        while (true) {
            try {
                Socket connection = listener.accept();
                pool.submit(new TrackTask(connection));
            }
            catch (IOException ex) {
                log.error("Exception in tracker server main listen-loop");
                log.error(ex.getMessage());
            }
        }
    }


    class TrackTask extends Thread {

        private Socket socket;
        BufferedReader in;
        PrintWriter out;

        public InetSocketAddress getInetSocketAddr() {
            return new InetSocketAddress(localIPAddr, listener.getLocalPort());
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

                log.info("received command: "+command);

                switch (command) {
                    case Common.ADD_FILE_CMD: receiveFileToTrack(); break;
                    case Common.LIST_FILES_CMD: listFiles(); break;
                    case Common.GET_SEEDERS_CMD: sendSeeders(); break;
                    default: throw new RuntimeException("UNKNOWN COMMAND: "+command);
                }

            }
            catch (IOException ex) { log.error(ex.getMessage()); }
            catch (ClassNotFoundException e) { e.printStackTrace(); }
            finally {
                try { socket.close(); }
                catch (IOException e) { log.error(e.getMessage());}
            }
        }

        void receiveFileToTrack() throws IOException, ClassNotFoundException {
            ObjectOutputStream objOut = Common.objectOStream(socket);
            ObjectInputStream objIn = Common.objectIStream(socket);
            InetSocketAddress peerIPAddr = (InetSocketAddress) objIn.readObject();
            P2PFileMetadata rcvdMeta = (P2PFileMetadata) objIn.readObject();
            String filename = rcvdMeta.getFilename();

            // TODO these status codes ought to be unit tested

            if (swarmsByFilename.containsKey(filename)) {
                Swarm swarm = swarmsByFilename.get(filename);
                if (swarm.pFileMetadata.equals(rcvdMeta)) {
                    if (swarm.getSeeders().contains(peerIPAddr)) {
                        objOut.writeObject(Common.StatusCodes.ALREADY_LISTED);
                    } else {
                        swarm.addSeeder(peerIPAddr);
                        objOut.writeObject(Common.StatusCodes.ADDR_ADDED);
                    }
                }
                else {
                    objOut.writeObject(Common.StatusCodes.METADATA_MISMATCH);
                }
            }
            else { /* no existing swarm for this filename */

                try {
                    Swarm swarm = new Swarm(peerIPAddr, rcvdMeta);
                    swarmsByFilename.put(filename, swarm);

                    // this is for synchronization while running on one machine
                    synchronized (swarmsByFilename) {
                        swarmsByFilename.notifyAll();
                    }
                    objOut.writeObject(Common.StatusCodes.SWARM_CREATED);
                }
                catch (Exception e) {
                    log.error(e.getMessage());
                    objOut.writeObject(Common.StatusCodes.SERVER_EXCEPTION);
                }
            }
        }

        void listFiles() throws IOException {
            ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
            objOut.flush();
            if (swarmsByFilename.isEmpty()) {
                objOut.writeObject(Common.EMPTY_SWARMS);
            }
            else {
                for (Map.Entry<String, Swarm> entry : swarmsByFilename.entrySet()) {
                    objOut.writeObject(entry.getValue().pFileMetadata);
                    int swarmSize = entry.getValue().numSeeders();
                    objOut.writeInt(swarmSize);
                    objOut.flush(); // otw writeInt isn't written for some reason.
                }
            }
        }

        /**
         * read a P2PFileMetadata object
         * return SWARM_NOT_FOUND -- if it's name doesn't match any known swarm
         * return METADATA_MISMATCH -- if the metadata itself is incorrect for this file name
         * return Set<InetSocketAddress> -- otw
         */
        void sendSeeders() throws IOException, ClassNotFoundException {
            ObjectOutputStream objOut = Common.objectOStream(socket);
            ObjectInputStream objIn = Common.objectIStream(socket);
            P2PFileMetadata rcvdMeta = (P2PFileMetadata) objIn.readObject();
            String filename = rcvdMeta.getFilename();
            Swarm swarm = swarmsByFilename.get(filename);

            if (swarm == null) {
                objOut.writeObject(Common.StatusCodes.SWARM_NOT_FOUND);
                return;
            }

            if (!swarm.pFileMetadata.equals(rcvdMeta)) {
                objOut.writeObject(Common.StatusCodes.METADATA_MISMATCH);
                return;
            }

            objOut.writeObject(swarm.getSeeders());
        }
    }
}
