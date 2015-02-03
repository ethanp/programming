package na.ethanp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Pinger {
    public static final String pingTwoLines = "pingTwo 1st\n"+
                                              "pingTwo 2nd\n";

    public static void main(String[] args) throws IOException {
        String ipAddr = args[0];
        int port = Integer.parseInt(args[1]);
        System.out.print("pinger started...");
        System.out.println("connecting to "+args[0]+":"+args[1]);
        Socket socket = new Socket(ipAddr, port);
        System.out.println("pinger connected to address...");
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println(pingTwoLines);
        String inLine;
        while ((inLine = reader.readLine()) != null && !inLine.trim().isEmpty()) {
            System.out.println("Pinger rcvd: "+inLine);
        }
    }
}
