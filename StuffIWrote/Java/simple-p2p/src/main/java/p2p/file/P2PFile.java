package p2p.file;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.FileSystemException;
import java.nio.file.Path;
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

    /** FIELDS **/

    public P2PFileMetadata metadata;
    Chunk[] dataChunks;


    /** GETTERS **/

    public String filenameString()      { return metadata.filename.toString();  }
    public String base64Digest()        { return metadata.base64Digest();       }
    public Chunk getChunkNum(int idx)   { return dataChunks[idx];               }
    public int numChunks()              { return dataChunks.length;             }


    /** CONSTRUCTORS **/

    public P2PFile(String filename, InetSocketAddress trackerAddr)
            throws FileSystemException, FileNotFoundException
    {
        byte[] shaDigest = P2PFile.getSha256(filename);
        metadata = new P2PFileMetadata(filename, trackerAddr, shaDigest);

        File fileRef = new File(filename);
        if (!fileRef.exists())
            throw new FileSystemException("file doesn't exist");
        if (fileRef.isDirectory())
            throw new FileSystemException("directories are not supported at this time");
        if (!fileRef.canRead())
            throw new FileSystemException("file doesn't have read permissions");

        metadata.numBytes = fileRef.length();
        metadata.numChunks = (int) Math.ceil((double) metadata.numBytes / Chunk.BYTES_PER_CHUNK);
        dataChunks = new Chunk[metadata.numChunks];

        BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(fileRef));

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
        dataChunks = new Chunk[meta.numChunks];
        for (Chunk c : chunks)
            dataChunks[c.idx] = c;
    }


    /** PUBLIC INTERFACE **/

    public boolean writeToDiskInDir(String relativeDir) {
        File holdingDir = new File(relativeDir);

        log.printf(Level.INFO, "writing %s to dir %s",
                   filenameString(), holdingDir.getAbsolutePath());

        if (!holdingDir.exists()) {
            /* "Creates the directory named by this abstract pathname,
                including any necessary but nonexistent parent directories." */
            boolean created = holdingDir.mkdirs();
            if (!created) {
                log.debug("directory was not created");
                return false;
            } else {
                log.info("created directory "+holdingDir.getAbsolutePath());
            }
        } else {
            if (!holdingDir.isDirectory()) {
                log.info("Failed: given location is not a directory");
                return false;
            }
            if (!holdingDir.canWrite()) {
                log.info("Failed: need write permissions on directory");
                return false;
            }
            log.info("directory "+holdingDir.getAbsolutePath()+" already exists, that's fine");
        }

        final File dld = new File(holdingDir, filenameString());
        if (dld.exists()) {
            log.info("Failed: file already exists");
            return false;
        }
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dld))) {
            for (Chunk c : dataChunks)
                bos.write(c.data);
        }
        catch (IOException e) {
            log.error("write failed\n"+e.getMessage());
            e.printStackTrace();
        }

        return true;
    }


    /** UTILITY METHODS **/

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
        if (metadata == null) return p2PFile.metadata == null;
        return metadata.equals(p2PFile.metadata);
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

