package p2p.file;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Chunk implements Serializable {
    static final int BYTES_PER_CHUNK = 1 << 10; // 1KB
    byte[] data;
    int numBytes;
    byte[] chunkDigest;

    Chunk(byte[] data, int len) {
        this.data = data;
        numBytes = len;
        chunkDigest = makeDigest();
    }

    byte[] makeDigest() {
        MessageDigest digest = null;
        try { digest = MessageDigest.getInstance("SHA-256"); }
        catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        assert digest != null;
        digest.update(data, 0, numBytes);
        return digest.digest();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chunk)) return false;
        Chunk chunk = (Chunk) o;
        if (numBytes != chunk.numBytes) return false;
        if (!Arrays.equals(chunkDigest, chunk.chunkDigest)) return false;
        if (!Arrays.equals(data, chunk.data)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(data);
        result = 31*result+numBytes;
        result = 31*result+Arrays.hashCode(chunkDigest);
        return result;
    }

    public boolean verifyDigest() {
        /* a Chunk should always have a digest */
        if (chunkDigest == null) return false;

        /* the digest should match the data */
        return Arrays.equals(chunkDigest, makeDigest());
    }
}
