package p2p;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.URL;
import java.util.List;

/**
 * Ethan Petuchowski 12/29/14
 */
public class Peer {
    int DEFAULT_PORT = 3455;

    List<P2PTransfer> ongoingTransfers;
    List<P2PFile> registeredSeeding;
    List<P2PFile> registeredLeeching;

    void informTrackerAboutFile(URL trackerURL, String filename) {
        throw new NotImplementedException();
    }
}
