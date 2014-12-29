package p2p.server;

import p2p.file.P2PFile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.Socket;
import java.net.URL;
import java.util.List;

/**
 * Ethan Petuchowski 12/29/14
 */
public class Peer {
    int DEFAULT_PORT = 3455;

    List<OngoingTransfer> ongoingTransfers;
    List<P2PFile> registeredSeeding;
    List<P2PFile> registeredLeeching;

    void informTrackerAboutFile(URL trackerURL, String filename) {
        throw new NotImplementedException();
    }
}

class OngoingTransfer {
    P2PFile file;
    int firstChunk;
    int lastChunk;
    URL to;
    URL from;
}
