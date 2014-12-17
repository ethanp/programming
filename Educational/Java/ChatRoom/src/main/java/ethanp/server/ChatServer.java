package ethanp.server;

import ethanp.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

/**
 * This is based on
 * http://cs.lmu.edu/~ray/notes/javanetexamples/#chat
 *
 * - A multithreaded chat room server for a single room.
 * - Server requests a screenname via text "SUBMITNAME" until a unique one is received.
 * - Server acknowledges with "NAMEACCEPTED".
 * - Messages from that client are broadcast to all other clients, prefixed with "MESSAGE".
 *
 * Features Missing
 *
 *   1. Client should send a "DISCONNECT" message to leave
 *
 *   2. The server should do some logging.
 *
 *   3. The whole thing should be "zoomed" out so that a single server
 *      can handle *multiple* rooms simultaneously
 *
 */
public class ChatServer {

    /** The port that the server listens on. */
    private static final int PORT = Constants.PORT_NO;

    /** Set of names of clients in the chat room. */
    private static HashSet<String> names = new HashSet<>();

    /** For broadcasting messages. */
    private static HashSet<PrintWriter> writers = new HashSet<>();

    /** Listens on port and spawn handler threads. */
    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running.");
        ServerSocket socketListener = new ServerSocket(PORT);
        try {
            while (true) {
                // note that each (normal) socket has exactly 2 endpoints (client & server)
                Socket socket = socketListener.accept();
                new Handler(socket).start();
            }
        } finally {
            socketListener.close();
        }
    }

    /** Responsible for a dealing with a single client and broadcasting its messages. */
    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        /** Save a reference to the socket. */
        public Handler(Socket socket) {
            this.socket = socket;
        }

        /**
         * - Request screenname until a unique one has been submitted.
         * - Ack the name and register output stream for client in the global set.
         * - Repeatedly get inputs and broadcast them.
         */
        public void run() {
            try {

                // Create buffered char stream for the socket so we can readline() it
                in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));

                out = new PrintWriter(socket.getOutputStream(), true);

                // Request name from client.
                // Checking for and adding the name must be synchronized.
                while (true) {
                    out.println("SUBMITNAME"); // send msg to client
                    name = in.readLine();      // wait for and rcv response

                    if (name == null) { // null means "end of stream has been reached"
                        return;         // so we kill the thread
                    }

                    synchronized (names) {
                        if (!names.contains(name)) {
                            names.add(name);
                            break;     // success! OK to stop requesting a name now
                        }
                    }
                }

                // Send "success" and save the socket's print writer.
                out.println("NAMEACCEPTED");
                writers.add(out);

                // Broadcast client's messages.
                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + ": " + input);
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                // Client left!
                // Remove its name and its print writer from the sets, and close its socket.
                if (name != null) {
                    names.remove(name);
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
