package ethanp.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Ethan Petuchowski 12/16/14
 * based on DaytimeClient from
 * "Java Network Programming" 4ed, Harold, (O'Reilly)
 */
public class SimpleClient {
    public static void main(String[] args) {

        String hostname = args.length > 0 ? args[0] : "localhost";
        Socket socket = null;
        try {
            socket = new Socket(hostname, 1175);
            socket.setSoTimeout(15000);
            InputStream in = socket.getInputStream();
            StringBuilder time = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(in, "ASCII");
            for (int c = reader.read(); c != -1; c = reader.read()) {
                time.append((char) c);
            }
            System.out.println(time);
        } catch (IOException ex) {
            System.err.println(ex);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    // ignore
                }
            }
        }
    }
}
