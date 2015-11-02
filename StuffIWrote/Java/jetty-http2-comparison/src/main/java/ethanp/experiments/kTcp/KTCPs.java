package ethanp.experiments.kTcp;


import org.apache.commons.lang3.RandomUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Ethan Petuchowski 11/2/15
 *
 * The point of this class is to provide an interface to send a large file via "k" concurrent
 * TCP connections, for any given value of "k".
 */
public class KTCPs {

    ExecutorService threadPool;
    int firstPort;
    int numServers;
    int bytesSentPerConn;

    public KTCPs(int numServers, int firstPort, int bytesSentPerConn) {
        assert firstPort >= 0 : "can't have negative port number";
        assert numServers >= 1 : "require at least one concurrent connection";
        assert bytesSentPerConn >= 1 : "must send > 0 bytes";

        this.numServers = numServers;
        this.firstPort = firstPort;
        this.bytesSentPerConn = bytesSentPerConn;

        this.threadPool = Executors.newFixedThreadPool(numServers);
        for (int i = 0; i < numServers; i++) {
            NonPersistent np = new NonPersistent(firstPort+i, bytesSentPerConn);
            threadPool.execute(np);
        }
    }

    /**
     * start a server
     * wait for a client to connect
     * on connect, send data immediately
     * time how long this takes (server side time taken)
     */
    static class NonPersistent implements Runnable {

        int numBytes;
        int port;
        boolean isServing;

        NonPersistent(int port, int numBytes) {
            this.port = port;
            this.numBytes = numBytes;
        }

        /**
         * we enforce "crash failure semantics" so that there are no little
         * errors that screw up my data without much notification to me.
         */
        @Override public void run() {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("serving "+numBytes+" bytes at port "+port);
                isServing = true;
                while (isServing) {
                    Socket clientSocket = serverSocket.accept();
                    long start = System.nanoTime();
                    try (OutputStream os = clientSocket.getOutputStream()) {
                        os.write(RandomUtils.nextBytes(numBytes));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        System.exit(3);
                    }
                    long end = System.nanoTime();
                    System.out.println("server at "+port+" took "+millis(start, end)+" ms");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                System.exit(2);
            }
        }

        private static long millis(long start, long end) {
            return (end-start)/1_000_000;
        }
    }

    public static void main(String[] args) {
        KTCPs a = new KTCPs(3, 2500, 25);
    }
}
