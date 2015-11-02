package ethanp.experiments.kTcp;

import org.junit.Test;
import org.junit.runners.model.TestTimedOutException;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Ethan Petuchowski 11/2/15
 */
public class KTCPsTest {

    @Test
    public void testBasic() throws Exception {
        int numServers = 30;
        int firstPort = 2500;
        int bytesPerConn = 2500;
        new KTCPs(numServers, firstPort, bytesPerConn);
        ExecutorService clientPool = Executors.newFixedThreadPool(numServers);
        for (int i = 0; i < numServers; i++) {
            clientPool.execute(new TCPClient(firstPort+i));
        }
        clientPool.shutdown();
        if (!clientPool.awaitTermination(10, TimeUnit.SECONDS)) {
            throw new TestTimedOutException(10, TimeUnit.SECONDS);
        }
    }

    static class TCPClient implements Runnable {

        int port;

        TCPClient(int port) {
            this.port = port;
        }

        @Override public void run() {
            try (Socket serverConn = new Socket(InetAddress.getLocalHost(), port)) {
                try (InputStream is = serverConn.getInputStream()) {
                    int ctr = 0;
                    while (is.read() != -1) ctr++;
                    System.out.println("received "+ctr+" bytes at port "+port);
                }
                catch (IOException e) {
                    e.printStackTrace();
                    System.exit(6);
                }
            }
            catch (UnknownHostException e) {
                e.printStackTrace();
                System.exit(4);
            }
            catch (IOException e) {
                e.printStackTrace();
                System.exit(5);
            }
        }
    }
}
''
