package p2p;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Ethan Petuchowski 12/29/14
 *
 * based on the PooledDaytimeServer in "Java Network Programming"
 */
public class Tracker {

    public final static int PORT = 13;

    ConcurrentSkipListSet<Swarm> swarms;

    URL trackerURL; // TODO is this the right way to do this?

    public static String contactInfo() {
        throw new NotImplementedException();
    }

    public static void main(String[] args) {

        ExecutorService pool = Executors.newFixedThreadPool(5);

        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                try {
                    Socket connection = server.accept();
                    Callable<Void> task = new TrackerTask(connection);
                    pool.submit(task);
                }
                catch (IOException ex) {}
            }
        }
        catch (IOException ex) {
            System.err.println("Couldn't start server");
        }
    }
}

class TrackerTask implements Callable<Void> {

    private Socket connection;

    TrackerTask(Socket connection) {
        this.connection = connection;
    }

    @Override
    public Void call() {
        try {
            Writer out = new OutputStreamWriter(connection.getOutputStream());
            Date now = new Date();
            out.write(now.toString()+"\r\n");
            out.flush();
        }
        catch (IOException ex) {
            System.err.println(ex);
        }
        finally {
            try {
                connection.close();
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

