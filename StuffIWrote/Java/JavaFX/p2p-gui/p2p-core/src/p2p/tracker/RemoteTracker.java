package p2p.tracker;

import p2p.file.P2PFile;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Ethan Petuchowski 1/15/15
 *
 * This contains the info that a Peer knows about a Tracker.
 */
public class RemoteTracker extends Tracker {

    public RemoteTracker(InetSocketAddress addr) {
        super(addr);
    }

    public void requestInfo() {
    }

    public void updateSwarmAddrs(P2PFile pFile) {
    }
}
