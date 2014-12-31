package p2p;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * One of these is held for each P2PFile maintained by a Tracker
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
