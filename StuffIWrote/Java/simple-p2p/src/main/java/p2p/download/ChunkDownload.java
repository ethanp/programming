package p2p.download;

import p2p.file.Chunk;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Ethan Petuchowski 1/3/15
 */
public class ChunkDownload implements Callable<Chunk> {

    Set<InetSocketAddress> hostPeers = new HashSet<>();

    P2PDownload p2pDownload;

    ChunkDownload(Set<InetSocketAddress> peers) {
        hostPeers = peers;
    }

    /**
     * Downloads a Chunk, or throws an exception if unable to do so.
     * @return downloaded Chunk
     * @throws Exception if unable to compute a result
     * @throws java.util.concurrent.TimeoutException if download is too slow
     */
    @Override
    public Chunk call() throws Exception {

        // open a Socket to the "addr" with an ObjectStream pair

        // download a Chunk (with timeout)
        Chunk chunk;
        return null;
    }
}
