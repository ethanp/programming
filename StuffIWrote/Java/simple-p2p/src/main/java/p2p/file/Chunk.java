package p2p.file;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Chunk implements Serializable {
    static final int BYTES_PER_CHUNK = 1 << 10; // 1KB
    byte[] data;
    int numBytes;
    public int idx;
    byte[] chunkDigest;

    Chunk(byte[] data, int len, int idxWithinFile) {
        this.data = data;
        idx = idxWithinFile;
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

        return numBytes == chunk.numBytes
            && idx == chunk.idx
            && Arrays.equals(chunkDigest, chunk.chunkDigest)
            && Arrays.equals(data, chunk.data);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(data);
        result = 31*result+numBytes;
        result = 31*result+Arrays.hashCode(chunkDigest);
        return result;
    }

    public boolean verifyDigest() {
        return chunkDigest != null
            && Arrays.equals(chunkDigest, makeDigest());
    }
}
