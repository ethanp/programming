package p2p;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sun.security.provider.SHA2;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Ethan Petuchowski 12/29/14
 */
public class P2PFile {

    /* Allowing P2PFile to be a Directory full of files is "future work",
     * as in I've never dealt with entire directories before so I'm leaving it alone for now.
     */

    /* class Path is strictly better than class File */
    Path filename;

    /* Allowing for multiple tracker URLs is probably easy, and left for future work */
    URL trackerURL;

    /* this is the thing you have to be able to obtain in a trustworthy manner */
    byte[] sha256Digest;

    Chunk[] dataChunks;

    Chunk getChunkNum(int chunkNum) {
        throw new NotImplementedException();
    }

    int numChunks() {
        return dataChunks.length;
    }

    P2PFile(String fileName) {
        filename = Paths.get(fileName);
        try { storeSha256(); }
        catch (NoSuchAlgorithmException | IOException e) { e.printStackTrace(); }
    }

    void storeSha256() throws IOException, NoSuchAlgorithmException {
        byte[] buffer = new byte[1024];
        int readBytes;
        MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
        try (InputStream is = Files.newInputStream(filename)) {
            while ((readBytes = is.read(buffer)) > 0) {
                msgDigest.update(buffer, 0, readBytes);
            }
        }
        sha256Digest = msgDigest.digest();
    }
}

class Chunk {
    byte[] bytes;
    int sizeInBytes;
    SHA2 dataHash;
    SHA2 p2pFileHash;
}
