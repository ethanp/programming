package p2p;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

/**
 * - One of these is held for each P2PFile maintained by a Tracker
 *
 * - When a peer wants to download,
 *   he receives the addresses of all the seeders contained within
 */
public class Swarm {

    static final Logger log = LogManager.getLogger(Swarm.class.getName());

    /* this is where the DHT will live */

    P2PFileMetadata pFileMetadata;
    private HashSet<InetSocketAddress> seeders = new HashSet<>();

    int numSeeders() {
        return seeders.size();
    }

    Set<InetSocketAddress> getSeeders() {
        return seeders;
    }

    Swarm(InetSocketAddress initialSeederAddress, P2PFileMetadata pFileMetadata) {
        this.pFileMetadata = pFileMetadata;
        seeders.add(initialSeederAddress);
    }

    void addSeeder(InetSocketAddress address) {
        seeders.add(address);
    }
}
