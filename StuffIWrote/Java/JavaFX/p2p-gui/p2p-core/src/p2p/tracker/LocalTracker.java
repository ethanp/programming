package p2p.tracker;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Ethan Petuchowski 1/15/15
 *
 * This contains the info that a Tracker knows about itself.
 */
public class LocalTracker extends Tracker {

    public LocalTracker(InetSocketAddress addr) {
        super(addr);
    }

    public LocalTracker(List<Swarm> swarms, InetSocketAddress addr) {
        super(addr, swarms);
    }
}
