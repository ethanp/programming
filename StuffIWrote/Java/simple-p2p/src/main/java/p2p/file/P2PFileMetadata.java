package p2p.file;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Arrays;

public class P2PFileMetadata implements Comparable<P2PFileMetadata>, Serializable {

    static final Logger log = LogManager.getLogger(P2PFileMetadata.class.getName());

    String filename;

    /* Allowing for multiple tracker URLs is probably easy, and left for future work */
    InetSocketAddress trackerAddr;

    /* this is the thing you have to be able to obtain in a trustworthy manner */
    byte[] sha2Digest;

    /* not used for metadata equality */
    long numBytes = -1;
    int numChunks = -1;

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    public byte[] getSha2Digest() { return sha2Digest; }
    public void setSha2Digest(byte[] sha2Digest) { this.sha2Digest = sha2Digest; }
    public long getNumBytes() { return numBytes; }
    public int getNumChunks() { return numChunks; }

    P2PFileMetadata(String filename,
                    InetSocketAddress trackerAddr,
                    byte[] sha2Digest)
    {
        this.filename = filename;
        this.trackerAddr = trackerAddr;
        this.sha2Digest = sha2Digest;
    }

    /* doesn't implement Cloneable because Josh Bloch says clone is BAD and
       I don't want to take the time now to learn why */
    public P2PFileMetadata clone() {
        return new P2PFileMetadata(
                filename,
                new InetSocketAddress(trackerAddr.getAddress(), trackerAddr.getPort()),
                Arrays.copyOf(sha2Digest, sha2Digest.length));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof P2PFileMetadata)) return false;
        P2PFileMetadata that = (P2PFileMetadata) o;
        if (filename != null ? !filename.equals(that.filename)
                             : that.filename != null)
            return false;
        if (!Arrays.equals(sha2Digest, that.sha2Digest)) return false;
        if (trackerAddr != null ? !trackerAddr.equals(that.trackerAddr)
                                : that.trackerAddr != null)
            return false;
        if (numBytes != that.numBytes) return false;
        if (numChunks != that.numChunks) return false;

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
        result = 31*result+(sha2Digest != null ? Arrays.hashCode(sha2Digest) : 0);
        return result;
    }

    public String base64Digest() {
        return DatatypeConverter.printBase64Binary(sha2Digest);
    }
}
