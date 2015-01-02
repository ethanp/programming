package p2p;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

/**
 * Ethan Petuchowski 12/29/14
 */
public class Common {

    public enum StatusCodes {
        SUCCESS,
        NO_INTERNET
    }

    static final String ADD_FILE_CMD = "add file";
    static final String LIST_FILES_CMD = "list tracked files";
    static final String GET_SEEDERS_CMD = "get seeders for file";

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
            System.err.println("Peer not connected to Internet: can't find its IP");
        }
        finally {
            if (in != null) {
                try { in.close(); }
                catch (IOException e) { e.printStackTrace(); }
            }
        }
        return toRet;
    }
}
