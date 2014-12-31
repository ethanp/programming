package p2p;

import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PeerTest {

    static final String SAMPLE_FILENAME = "asdfile";

    Peer peer;
    Tracker tracker;
    P2PFile sampleP2PFile;

    @Before
    public void setUp() throws Exception {
        tracker = new Tracker();
        tracker.start();
        InetSocketAddress trackerAddr = tracker.getInetSocketAddr();

        peer = new Peer();
        peer.setTracker(trackerAddr);

        sampleP2PFile = new P2PFile(SAMPLE_FILENAME, trackerAddr);
    }

    /**
     * register sample file with tracker
     */
    void shareSampleFile() {
        peer.shareFile(SAMPLE_FILENAME);
    }

    /**
     * peer knows it is sharing the file
     */
    @Test
    public void testShareFilePeerSeeding() throws Exception {
        shareSampleFile();
        assertTrue(peer.completeAndSeeding.contains(sampleP2PFile));
    }

    /**
     * tracker maintains swarm for file
     */
    @Test
    public void testShareFileTrackerTrackingName() throws Exception {
        shareSampleFile();
        assertTrue(tracker.isTrackingFilename(SAMPLE_FILENAME));
    }

    /**
     * tracker's swarm is correct
     */
    @Test
    public void testShareFileTrackerSwarm() throws Exception {
        shareSampleFile();
        P2PFileMetadata trueMeta = sampleP2PFile.metadata;
        ConcurrentSkipListMap<String, Swarm> swarmMap = tracker.swarmsByFilename;
        assertEquals(1, swarmMap.size());
        Swarm sampleSwarm = swarmMap.get(SAMPLE_FILENAME);
        assertEquals(1, sampleSwarm.seeders.size());
        P2PFileMetadata savedMeta = sampleSwarm.pFileMetadata;
        assertEquals(trueMeta, savedMeta);
    }
}
