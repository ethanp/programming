package p2p;

import p2p.file.P2PFileMetadata;
import p2p.peer.Peer;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystemException;
import java.util.List;
import java.util.Scanner;

/**
 * Ethan Petuchowski 1/4/15
 */
public class P2PConsole {
    static final String startString =
            "\nEnter 'peer' or 'tracker'\n" +
            "Note that there must be at least one tracker instance running" +
            "for this to work.\n";

    static final String peerString =
            "\nCommands:\n" +
            "set tracker <trackerIP>\n" +
            "list\n" +
            "upload <filename>\n" +
            "download <listNo>\n" +
            "quit\n";

    Scanner scanner;
    Peer peer;
    Tracker tracker;
    List<P2PFileMetadata> trackerListing;

    public static void main(String[] args) {
        new P2PConsole();
    }

    P2PConsole() {
        System.out.println(startString);
        scanner = new Scanner(System.in);
        boolean deployed = false;
        while (!deployed) {
            String type = scanner.nextLine();
            switch (type) {
                case "peer":
                    deployed = true;
                    startPeer();
                    break;
                case "tracker":
                    deployed = true;
                    startTracker();
                    break;
                default:
                    System.out.println("Command not recognized!\n"+startString);
                    break;
            }
        }
    }

    void startPeer() {
        System.out.println("starting peer...");
        peer = new Peer(this);
    }

    void startTracker() {
        System.out.println("starting tracker...");
        tracker = new Tracker(this);
        tracker.start();
    }

    public P2PConsole putIPAddr(InetAddress ipAddr, int portNo) {
        System.out.println("listening at address "
                           +ipAddr.toString().substring(1)+":"+portNo);
        return this;
    }

    public void runPeerConsole() {
        while (true) {
            System.out.println(peerString);
            String cmd = scanner.nextLine();
            String[] parts = cmd.split(" ");
            if (setTrackerCmd(cmd)) {
                if (parts.length != 3) {
                    System.out.println("wrong format, try again");
                    continue;
                }
                try {
                    InetSocketAddress trkrAddr = Common.addrFromString(parts[2]);
                    peer.setTracker(trkrAddr);
                }
                catch (UnknownHostException e) {
                    System.out.println("unknown host! please try again.");
                    continue;
                }
                System.out.println("tracker set successfully");
            }
            else if (listCmd(cmd)) {
                if (parts.length != 1) {
                    System.out.println("wrong format, try again");
                    continue;
                }
                try {
                    trackerListing = peer.listSavedTracker();
                }
                catch (ConnectException e) {
                    System.out.println("connection to tracker refused! " +
                                       "try a different address.");
                    continue;
                }
                printListResult(trackerListing);
            }
            else if (uploadCmd(cmd)) {
                if (parts.length != 2) {
                    System.out.println("wrong format, try again");
                    continue;
                }
                try {
                    peer.shareFile(parts[1]);
                }
                catch (FileNotFoundException | FileSystemException e) {
                    System.out.println(e.getMessage());
                }
            }
            else if (downloadCmd(cmd)) {
                if (parts.length != 2) {
                    System.out.println("wrong format, try again");
                    continue;
                }
                System.out.printf(
                        "Downloading \"%s\"\n",
                        trackerListing.get(Integer.parseInt(parts[1])));
            }
            else if (quitCmd(cmd)) {
                System.out.println("exiting");
                System.exit(0);
            }
        }
    }

    boolean setTrackerCmd(String cmd) { return cmd.startsWith("set tracker"); }
    boolean listCmd(String cmd)       { return cmd.startsWith("list");        }
    boolean uploadCmd(String cmd)     { return cmd.startsWith("upload");      }
    boolean downloadCmd(String cmd)   { return cmd.startsWith("download");    }
    boolean quitCmd(String cmd)       { return cmd.startsWith("quit");        }
    boolean in(int v, int s, int l)   { return s <= v && v <= l;              }
    boolean optTrk(String[] arr, int b) {
        return peer.trkAddr == null ? arr.length != b
                                    : !in(arr.length, b-1, b);
    }

    void printListResult(List<P2PFileMetadata> metaList) {
        if (metaList.isEmpty()) {
            System.out.println("It appears the tracker is empty");
        } else {
            System.out.println("The requested tracker lists the following files:");
            int i = 0;
            for (P2PFileMetadata meta : metaList) {
                ++i;
                System.out.printf("%d) \"%s\" %d B\n",
                                  i, meta.getFilename(), meta.getNumBytes());
            }
        }
    }
}
