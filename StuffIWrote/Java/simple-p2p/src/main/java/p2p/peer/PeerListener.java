package p2p.peer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import p2p.Common;
import p2p.P2PTransfer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeerListener extends Thread {

    static final Logger log = LogManager.getLogger(PeerListener.class.getName());
    int listeningPort;

    @Override
    public void run() {

        ExecutorService threadPool = Executors.newFixedThreadPool(50);

        // "0" finds a free port: stackoverflow.com/questions/2675362
        // and I was using that, but I just set my router to forward 3-3.5K to me
        try (ServerSocket listener = Common.socketPortInRange(Common.PORT_MIN, Common.PORT_MAX)) {
            listeningPort = listener.getLocalPort();
            while (true) {
                Socket conn = listener.accept();
                // TODO or maybe it should be submit(new P2PTransfer(conn))??
                threadPool.submit(new PeerServeTask(conn));
            }
        }
        catch (IOException e) {
            log.error("Peer not connected to Internet: can't contact tracker");
            e.printStackTrace();
            System.exit(Common.StatusCodes.NO_INTERNET.ordinal());
        }
    }

    class PeerServeTask extends Thread {

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
            // TODO see above, maybe this shouldn't exist
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
