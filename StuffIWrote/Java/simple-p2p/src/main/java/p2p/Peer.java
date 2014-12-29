package p2p;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Ethan Petuchowski 12/29/14
 *
 * Peers act as
 *      Clients for leeching
 *      Servers for seeding
 */
public class Peer {

    int DEFAULT_PORT = 2102;
    InetAddress ipAddr;
    Path localDir;

    List<P2PTransfer> ongoingTransfers;
    List<P2PFile> completeAndSeeding;

    void informTrackerAboutFile(URL trackerURL, String filename) {
        throw new NotImplementedException();
    }

    Peer(String dirString) {
        localDir = Paths.get(dirString);
        findMyIP();
    }

    void findMyIP() {
        URL aws = null;
        try { aws = new URL("http://checkip.amazonaws.com"); }
        catch (MalformedURLException e) { e.printStackTrace(); }
        BufferedReader in = null;
        try {
            if (aws != null) {
                in = new BufferedReader(new InputStreamReader(aws.openStream()));
                String ip = in.readLine();
                ipAddr = InetAddress.getByName(ip);
            }
        }
        catch (IOException e) { e.printStackTrace(); }
        finally {
            if (in != null) {
                try { in.close(); }
                catch (IOException e) { e.printStackTrace(); }
            }
        }
    }

    void shareFile(String pathString) {
        // TODO create a P2PFile and informTrackerAboutIt()
    }

    static class SeedTask extends Thread {
        String filename;
        Socket socket;
        BufferedReader in;
        BufferedWriter out;

        public SeedTask(String filename, Socket socket) {
            this.filename = filename;
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            }
            catch (IOException e) { e.printStackTrace(); }
        }

        @Override
        public void run() {

            // TODO no this is not supposed to just be an echo server

            while (true) {
                try {
                    String msg = "Received: " + in.readLine();
                    System.out.println(msg);
                    out.write(msg);
                    out.newLine();
                    out.flush();
                }
                catch (IOException e) { e.printStackTrace(); }
            }
        }
    }
}
