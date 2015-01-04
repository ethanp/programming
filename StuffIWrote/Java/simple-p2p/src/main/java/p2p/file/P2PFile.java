package p2p.file;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.FileSystemException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Ethan Petuchowski 12/29/14
 *
 * Allowing P2PFile to be a Directory full of files is "future work"
 */
public class P2PFile implements Comparable<P2PFile> {

    static final Logger log = LogManager.getLogger(P2PFile.class.getName());

    /* FIELDS */
    public P2PFileMetadata metadata;
    Chunk[] dataChunks;

    /* GETTERS */
    public String filenameString()          { return metadata.filename.toString(); }
    public String base64Digest()            { return metadata.base64Digest(); }
    public Chunk getChunkNum(int chunkNum)  { return dataChunks[chunkNum]; }
    public int numChunks()                  { return dataChunks.length; }

    public P2PFile(String filename, InetSocketAddress trackerAddr)
            throws FileSystemException, FileNotFoundException {
        byte[] shaDigest = P2PFile.getSha256(filename);
        metadata = new P2PFileMetadata(filename, trackerAddr, shaDigest);

        // TODO read the file in and create Chunks, etc.

        File fileRef = new File(filename);
        if (!fileRef.exists())
            throw new FileSystemException("file doesn't exist");
        if (fileRef.isDirectory())
            throw new FileSystemException("directories are not supported at this time");
        if (!fileRef.canRead())
            throw new FileSystemException("file doesn't have read permissions");

        metadata.numBytes = fileRef.length();
        metadata.numChunks = (int) Math.ceil((double)
                metadata.numBytes / Chunk.BYTES_PER_CHUNK);
        dataChunks = new Chunk[metadata.numChunks];

        BufferedInputStream fileIn =
                new BufferedInputStream(
                        new FileInputStream(fileRef));

        try {
            int chunkNum = 0;
            byte[] chunkData = new byte[Chunk.BYTES_PER_CHUNK];
            while (fileIn.available() > 0) {
                int size = fileIn.read(chunkData, 0, Chunk.BYTES_PER_CHUNK);
                dataChunks[chunkNum] = new Chunk(chunkData, size, chunkNum);
                chunkNum++;
            }

            if (chunkNum != dataChunks.length)
                throw new RuntimeException("file->chunk[] didn't work properly");
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public P2PFile(P2PFileMetadata meta, Collection<Chunk> chunks) {
        metadata = meta;
        dataChunks = (Chunk[]) chunks.toArray();
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

