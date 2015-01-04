package p2p;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import p2p.file.P2PFile;
import p2p.file.P2PFileMetadata;
import p2p.peer.Peer;

import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.nio.file.FileSystemException;

/**
 * Ethan Petuchowski 1/3/15
 */
public class BaseTest {
    protected static final String SAMPLE_FILENAME = "asdfile";

    protected Peer peer;
    protected Peer peer2;

    protected Tracker tracker;

    protected P2PFile sampleP2PFile;
    protected P2PFileMetadata sampleMeta;

    /* "The ExpectedException rule allows you to verify
        that your code throws a specific exception."    */
    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        tracker = new Tracker();
        tracker.start();

        InetSocketAddress trackerAddr = tracker.getInetSocketAddr();

        peer = new Peer();
        peer.setTracker(trackerAddr);

        peer2 = new Peer();
        peer2.setTracker(trackerAddr);

        sampleP2PFile = new P2PFile(SAMPLE_FILENAME, trackerAddr);
        sampleMeta = sampleP2PFile.metadata;
        try { peer.shareFile(SAMPLE_FILENAME); }
        catch (FileNotFoundException | FileSystemException e) { e.printStackTrace(); }
    }
}
