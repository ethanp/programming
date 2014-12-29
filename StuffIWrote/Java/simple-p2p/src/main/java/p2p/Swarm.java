package p2p;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * One of these is held for each P2PFile maintained by a Tracker
 */
public class Swarm {
    /* this is where the DHT will live */
    P2PFile file;
    ConcurrentSkipListSet<Peer>[] seedersByChunk; // one for each chunk

    Swarm(Peer initialSeeder, P2PFile pFile) {
        file = pFile;
        for (int i = 0; i < file.numChunks(); i++) {
            seedersByChunk[i] = new ConcurrentSkipListSet<>();
            seedersByChunk[i].add(initialSeeder);
        }
    }
}
