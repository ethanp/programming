package p2p.peer;

import java.net.InetSocketAddress;

/**
 * Ethan Petuchowski 1/8/15
 */
public class FakePeer extends Peer {
    public FakePeer(String fakeAddr) {
        super(new InetSocketAddress("these codes don't run", 2));
    }
}
