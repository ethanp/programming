package p2p;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sun.security.provider.SHA2;

import java.io.File;
import java.net.URL;
import java.util.Collection;

/**
 * Ethan Petuchowski 12/29/14
 */
public class P2PFile {

    /* allowing P2PFile to be directory full of files is "future work" */

    String filename;
    Collection<URL> trackerURLs;

    /* this is the thing you have to be able to obtain in a trustworthy manner */
    SHA2 dataHash;

    Chunk[] dataChunks;

    Chunk getChunkNum(int chunkNum) {
        throw new NotImplementedException();
    }

    int numChunks() {
        return dataChunks.length;
    }

    P2PFile(String fileName) {
        throw new NotImplementedException();
    }
}

class Chunk {
    byte[] bytes;
    int sizeInBytes;
    SHA2 dataHash;
    SHA2 p2pFileHash;
}
