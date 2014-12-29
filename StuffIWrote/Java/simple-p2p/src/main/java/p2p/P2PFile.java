package p2p;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sun.security.provider.SHA2;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Ethan Petuchowski 12/29/14
 */
public class P2PFile {

    /* Allowing P2PFile to be a Directory full of files is "future work",
     * as in I've never dealt with entire directories before so I'm leaving
     * it alone for now.  Part of it would likely entail using a
     * Collection<Path> instead of a single Path.
     */

    P2PFileMetadata metadata;

    Chunk[] dataChunks;

    public String filenameString() {
        return metadata.filename.toString();
    }

    public String base64Digest() {
        return DatatypeConverter.printBase64Binary(metadata.sha256Digest);
    }

    Chunk getChunkNum(int chunkNum) {
        throw new NotImplementedException();
    }

    int numChunks() {
        return dataChunks.length;
    }

    P2PFile(String filename, InetAddress trackerAddr) {
        byte[] shaDigest = P2PFile.getSha256(filename);
        metadata = new P2PFileMetadata(filename, trackerAddr, shaDigest);
    }

    /**
     * TODO not even sure this works bc I changed the input stream's class
     */
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
}

class Chunk {
    byte[] bytes;
    int sizeInBytes;
    SHA2 dataHash;
    SHA2 p2pFileHash;
}

class P2PFileMetadata {
    String filename;

    /* Allowing for multiple tracker URLs is probably easy, and left for future work */
    InetAddress trackerAddr;

    /* this is the thing you have to be able to obtain in a trustworthy manner */
    byte[] sha256Digest;

    P2PFileMetadata(String filename, InetAddress trackerAddr, byte[] sha256Digest) {
        this.filename = filename;
        this.trackerAddr = trackerAddr;
        this.sha256Digest = sha256Digest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof P2PFileMetadata)) return false;
        P2PFileMetadata that = (P2PFileMetadata) o;
        if (filename != null
            ? !filename.equals(that.filename)
            : that.filename != null)
            return false;

        if (!Arrays.equals(sha256Digest, that.sha256Digest))
            return false;

        if (trackerAddr != null
            ? !trackerAddr.equals(that.trackerAddr)
            : that.trackerAddr != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = filename != null ? filename.hashCode() : 0;
        result = 31*result+(trackerAddr != null ? trackerAddr.hashCode() : 0);
        result = 31*result+(sha256Digest != null ? Arrays.hashCode(sha256Digest) : 0);
        return result;
    }
}
