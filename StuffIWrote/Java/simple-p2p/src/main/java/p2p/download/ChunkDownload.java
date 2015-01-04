package p2p.download;

import p2p.Common;
import p2p.file.Chunk;
import p2p.file.P2PFileMetadata;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Ethan Petuchowski 1/3/15
 */
public class ChunkDownload implements Callable<Chunk> {

    Set<InetSocketAddress> hostPeers = new HashSet<>();
    static Random random = new Random();
    P2PFileMetadata meta;
    int chunkIdx;

    ChunkDownload(P2PFileMetadata pFileMetadata,
                  int chunkIdx,
                  Set<InetSocketAddress> peers)
    {
        meta = pFileMetadata;
        this.chunkIdx = chunkIdx;
        hostPeers = peers;
    }

    public InetSocketAddress randomHost() {
        return new ArrayList<>(hostPeers).get(random.nextInt(hostPeers.size()));
    }

    /**
     * Downloads a Chunk, or throws an exception if unable to do so.
     * @return downloaded Chunk
     * @throws Exception if unable to compute a result
     * @throws java.util.concurrent.TimeoutException if download is too slow
     */
    @Override
    public Chunk call() throws Exception {

        InetSocketAddress hostAddr = randomHost();

        try (Socket socket = Common.socketAtAddr(hostAddr)) {
            ObjectOutputStream oos = Common.objectOStream(socket);
            ObjectInputStream ois = Common.objectIStream(socket);
            oos.writeObject(Common.DL_CHUNK_CMD);
            oos.writeInt(chunkIdx);
            oos.writeObject(meta);
            Chunk rcvdChunk = (Chunk) ois.readObject();
            return rcvdChunk;
        }
    }
}
