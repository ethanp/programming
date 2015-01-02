package p2p;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
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

    P2PFileMetadata metadata;

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

    P2PFile(String filename, InetSocketAddress trackerAddr) {
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

    @Override
    public int compareTo(P2PFile o) {
        return metadata.compareTo(o.metadata);
    }
}

class Chunk {
    static final int BYTES_PER_CHUNK = 1 << 10; // 1KB
    byte[] data;
    int numBytes;
    byte[] chunkDigest;

    Chunk(byte[] data, int len) {
        this.data = data;
        numBytes = len;
        makeDigest();
    }

    void makeDigest() {
        MessageDigest digest = null;
        try { digest = MessageDigest.getInstance("SHA-256"); }
        catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        if (digest == null) return;
        digest.update(data, 0, numBytes);
        chunkDigest = digest.digest();
    }
}

class P2PFileMetadata implements Comparable<P2PFileMetadata>, Serializable {

    static final Logger log = LogManager.getLogger(P2PFileMetadata.class.getName());

    String filename;

    /* Allowing for multiple tracker URLs is probably easy, and left for future work */
    InetSocketAddress trackerAddr;

    /* this is the thing you have to be able to obtain in a trustworthy manner */
    byte[] sha256Digest;

    /* not used for metadata equality */
    int numBytes = -1; // TODO fill these in
    int numChunks = -1;

    P2PFileMetadata(String filename, InetSocketAddress trackerAddr, byte[] sha256Digest) {
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
    public int compareTo(P2PFileMetadata that) {

        if (this.equals(that))
            return 0;

        if (!filename.equals(that.filename))
            return filename.compareTo(that.filename);

        if (trackerAddr.hashCode() == that.trackerAddr.hashCode())
            return trackerAddr.hashCode() - that.trackerAddr.hashCode();

        if (!base64Digest().equals(that.base64Digest()))
            return base64Digest().compareTo(that.base64Digest());

        return 0;
    }

    @Override
    public int hashCode() {
        int result = filename != null ? filename.hashCode() : 0;
        result = 31*result+(trackerAddr != null ? trackerAddr.hashCode() : 0);
        result = 31*result+(sha256Digest != null ? Arrays.hashCode(sha256Digest) : 0);
        return result;
    }

    public String base64Digest() {
        return DatatypeConverter.printBase64Binary(sha256Digest);
    }
}
