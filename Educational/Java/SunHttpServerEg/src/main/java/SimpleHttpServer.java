import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * Ethan Petuchowski 1/24/15
 *
 * Modified from: http://www.rgagnon.com/javadetails/java-have-a-simple-http-server.html
 *
 * Comments were either written by me or taken from the docs for these classes.
 */
public class SimpleHttpServer {

    public static void main(String[] args) throws Exception {

        /* the second parameter is the maximum number of queued incoming
           connections to allow on the listening socket */
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        /* associate some paths to controller actions */
        server.createContext("/info", new InfoHandler());
        server.createContext("/get", new GetHandler());

        /* http requests are handled in tasks submitted to the Executor object
         * using null (or just not calling this method) means the thread handling the task
         * is the thread created by the start() method (which accept()ed the socket I guess).
         * I suppose we just give this guy an Executors.fixedThreadPool(5) if we want to make
         * it (boundedly) multithreaded.
         */
        server.setExecutor(null);

        /* start the server in a new thread */
        server.start();
    }

    /**
     * send back a simple String message
     */
    static class InfoHandler implements HttpHandler {
        @Override public void handle(HttpExchange t) throws IOException {
            String infoString = "Use /get to download a text file";
            t.sendResponseHeaders(200, infoString.length());
            OutputStream os = t.getResponseBody();
            os.write(infoString.getBytes());
            os.close();
        }
    }

    /**
     * get the file-to-send and stuff it into the response body stream
     */
    static class GetHandler implements HttpHandler {
        @Override public void handle(HttpExchange t) throws IOException {
            Headers h = t.getResponseHeaders();
            h.add("Content-Type", "text/plain");

            File file = new File("/Users/Ethan/Desktop/delete.txt");
            byte[] bytearray = new byte[(int) file.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            bis.read(bytearray, 0, bytearray.length);

            /* must be called before getting the response body */
            t.sendResponseHeaders(200, file.length());

            OutputStream os = t.getResponseBody();
            os.write(bytearray, 0, bytearray.length);
            os.close();
        }
    }
}
