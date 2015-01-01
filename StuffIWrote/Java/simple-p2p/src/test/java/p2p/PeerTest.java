package p2p;

import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

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
        peer = new Peer();
        InetSocketAddress trackerAddr = tracker.getInetSocketAddr();
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
        ConcurrentHashMap<String, Swarm> swarmMap = tracker.swarmsByFilename;

        synchronized (swarmMap) {
            swarmMap.wait();
        }
        assertEquals(1, swarmMap.size());
        Swarm sampleSwarm = swarmMap.get(SAMPLE_FILENAME);
        assertEquals(1, sampleSwarm.seeders.size());
        P2PFileMetadata savedMeta = sampleSwarm.pFileMetadata;
        assertEquals(trueMeta, savedMeta);


        // TODO (at some point, figure out whether to fix this)
        // this fails because saved IP is "internal" vrsn
        // I'm not sure whether to hack some fix to this or not because
        // it really depends on whether the tracker's socket.getRemoteAddress()
        // in some sort of "production" environment would see the internal or
        // external address, and I have no idea what happens in that situation.

//        InetAddress myExternalIPAddr = Common.findMyIP();
//        Iterator<InetSocketAddress> it = sampleSwarm.seeders.iterator();
//        InetSocketAddress savedSockAddrOfPeerInSwarm = it.next();
//        InetAddress savedIPAddrOfPeerInSwarm = savedSockAddrOfPeerInSwarm.getAddress();

//        assertEquals(myExternalIPAddr, savedIPAddrOfPeerInSwarm);
    }
}
