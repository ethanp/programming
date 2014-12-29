package p2p.tracker;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Ethan Petuchowski 12/29/14
 *
 * based on the PooledDaytimeServer in "Java Network Programming"
 */


public class TrackerTaskSubmitter {

  public final static int PORT = 13;

  public static void main(String[] args) {

   ExecutorService pool = Executors.newFixedThreadPool(5);

   try (ServerSocket server = new ServerSocket(PORT)) {
     while (true) {
       try {
         Socket connection = server.accept();
         Callable<Void> task = new TrackerTask(connection);
         pool.submit(task);
       } catch (IOException ex) {}
     }
    } catch (IOException ex) {
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
      out.write(now.toString() +"\r\n");
      out.flush();
    } catch (IOException ex) {
      System.err.println(ex);
    } finally {
      try {
        connection.close();
      } catch (IOException e) {
        // ignore;
      }
    }
    return null;
  }
}
