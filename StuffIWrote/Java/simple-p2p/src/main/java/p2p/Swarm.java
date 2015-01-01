package p2p;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.InetSocketAddress;
import java.util.HashSet;

/**
 * - One of these is held for each P2PFile maintained by a Tracker
 *
 * - When a peer wants to download,
 *   he receives the addresses of all the seeders contained within
 */
public class Swarm {
    /* this is where the DHT will live */
    P2PFileMetadata pFileMetadata;
    HashSet<InetSocketAddress> seeders = new HashSet<>();

    Swarm(InetSocketAddress initialSeederAddress, P2PFileMetadata pFileMetadata) {
        this.pFileMetadata = pFileMetadata;
        seeders.add(initialSeederAddress);
    }

    void addSeeder(InetSocketAddress address) {
        seeders.add(address);
    }

    /**
     * TODO for sending the seedersByChunk to a requesting client
     */
    public void serialized() {
        throw new NotImplementedException();
    }
}
