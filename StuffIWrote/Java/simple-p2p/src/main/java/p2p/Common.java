package p2p;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

/**
 * Ethan Petuchowski 12/29/14
 */
public class Common {

    static final Logger log = LogManager.getLogger(Common.class.getName());

    public enum StatusCodes {
        SUCCESS,
        NO_INTERNET,
        SWARM_FOUND,
        SWARM_NOT_FOUND,
        METADATA_MISMATCH,
        METADATA_MATCH
    }

    static final String ADD_FILE_CMD = "add file";
    static final String LIST_FILES_CMD = "list tracked files";
    static final String GET_SEEDERS_CMD = "get seeders for file";

    static final int PORT_MIN = 3000;
    static final int PORT_MAX = 3500;

    static BufferedReader bufferedReader(Socket s) {
        try { return new BufferedReader(new InputStreamReader(s.getInputStream())); }
        catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    static BufferedWriter bufferedWriter(Socket s) {
        try { return new BufferedWriter(new OutputStreamWriter(s.getOutputStream())); }
        catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    static PrintWriter printWriter(Socket s) {
        try { return new PrintWriter(s.getOutputStream(), true); }
        catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    static ObjectOutputStream objectOStream(Socket s) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        oos.flush();
        return oos;
    }

    static ObjectInputStream objectIStream(Socket s) throws IOException {
        return new ObjectInputStream(s.getInputStream());
    }

    static InetAddress findMyIP() {
        URL aws = null;
        InetAddress toRet = null;
        try { aws = new URL("http://checkip.amazonaws.com"); }
        catch (MalformedURLException e) { e.printStackTrace(); }
        BufferedReader in = null;
        try {
            if (aws != null) {
                in = new BufferedReader(new InputStreamReader(aws.openStream()));
                String ip = in.readLine();
                toRet = InetAddress.getByName(ip);
            }
        }
        catch (IOException e) {
            log.error("Peer not connected to Internet: can't find its IP");
        }
        finally {
            if (in != null) {
                try { in.close(); }
                catch (IOException e) { e.printStackTrace(); }
            }
        }
        return toRet;
    }

    static Socket socketAtAddr(InetSocketAddress addr) {
        try { return new Socket(addr.getAddress(), addr.getPort()); }
        catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    /**
     * the range is CLOSED on BOTH ends
     */
    static ServerSocket socketPortInRange(int start, int end) throws IOException {
        int[] ports = new int[end-start+1];
        for (int i = 0; i <= end-start; i++)
            ports[i] = i+start;
        return socketPortFromOptions(ports);
    }

    static ServerSocket socketPortFromOptions(int[] ports) throws IOException {
        for (int port : ports) {
            try {
                return new ServerSocket(port);
            }
            catch (IOException ex) {
                continue; /* try next port */
            }
        }
        throw new IOException("no free port found");
    }
}
