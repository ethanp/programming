package p2p;

import p2p.file.P2PFileMetadata;
import p2p.peer.Peer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.SortedSet;

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
            "trackerIPs become [optional] if you first 'set tracker ip'\n\n" +
            "set tracker <trackerIP>\n" +
            "list [<trackerIP>]\n" +
            "upload <file> [<trackerIP>]\n" +
            "download <filename> [<trackerIP>]\n" +
            "quit\n";

    Scanner scanner;
    Peer peer;
    Tracker tracker;
    SortedSet<P2PFileMetadata> trackerListResult;

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
                }
            }
            else if (listCmd(cmd)) {
                if (optTrk(parts, 2)) {
                    System.out.println("wrong format, try again");
                    continue;
                }
                if (parts.length == 2) {
                    InetSocketAddress addr;
                    try {
                        addr = Common.addrFromString(parts[1]);
                        trackerListResult = peer.listTracker(addr);
                    }
                    catch (UnknownHostException e) {
                        System.out.println("unknown host! please try again.");
                        continue;
                    }
                }
                else {
                    trackerListResult = peer.listSavedTracker();
                }

            }
            else if (uploadCmd(cmd)) {
                if (optTrk(parts, 3)) {
                    System.out.println("wrong format, try again");
                    continue;
                }
            }
            else if (downloadCmd(cmd)) {
                if (optTrk(parts, 3)) {
                    System.out.println("wrong format, try again");
                    continue;
                }

            }
            else if (quitCmd(cmd)) {
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
}
