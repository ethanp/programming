package p2p.file;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Ethan Petuchowski 12/29/14
 */
public class P2PFile implements Comparable<P2PFile> {

    static final Logger log = LogManager.getLogger(P2PFile.class.getName());


    /* Allowing P2PFile to be a Directory full of files is "future work",
     * as in I've never dealt with entire directories before so I'm leaving
     * it alone for now.  Part of it would likely entail using a
     * Collection<Path> instead of a single Path.
     */

    public P2PFileMetadata metadata;

    Chunk[] dataChunks;

    public String filenameString() {
        return metadata.filename.toString();
    }

    public String base64Digest() {
        return metadata.base64Digest();
    }

    Chunk getChunkNum(int chunkNum) {
        throw new NotImplementedException();
    }

    int numChunks() {
        return dataChunks.length;
    }

    public P2PFile(String filename, InetSocketAddress trackerAddr) {
        byte[] shaDigest = P2PFile.getSha256(filename);
        metadata = new P2PFileMetadata(filename, trackerAddr, shaDigest);
    }

    static byte[] getSha256(String filename) {
        byte[] buffer = new byte[1024];
        int numBytes;
        MessageDigest digest = null;
        try { digest = MessageDigest.getInstance("SHA-256"); }
        catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        BufferedInputStream bis = null;
        try { bis = new BufferedInputStream(new FileInputStream(filename)); }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        if (bis == null || digest == null) return null;
        try {
            while ((numBytes = bis.read(buffer)) > 0)
                digest.update(buffer, 0, numBytes);
        }
        catch (IOException e) {  e.printStackTrace(); }
        return digest.digest();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof P2PFile)) return false;
        P2PFile p2PFile = (P2PFile) o;
        if (!Arrays.equals(dataChunks, p2PFile.dataChunks)) return false;
        if (metadata != null ? !metadata.equals(p2PFile.metadata)
                             : p2PFile.metadata != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = metadata != null ? metadata.hashCode() : 0;
        result = 31*result+(dataChunks != null ? Arrays.hashCode(dataChunks) : 0);
        return result;
    }

    @Override
    public int compareTo(P2PFile o) {
        return metadata.compareTo(o.metadata);
    }
}

