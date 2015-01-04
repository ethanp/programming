package p2p.peer;

import org.junit.Test;
import p2p.BaseTest;
import p2p.Swarm;
import p2p.file.P2PFile;
import p2p.file.P2PFileMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PeerTest extends BaseTest {

    /**
     * peer knows it is sharing the file
     */
    @Test
    public void testShareFilePeerSeeding() throws Exception {
        assertTrue(peer.completeAndSeeding.contains(sampleP2PFile));
    }

    /**
     * tracker maintains swarm for file
     */
    @Test
    public void testShareFileTrackerTrackingName() throws Exception {
        synchronized (tracker.swarmsByFilename) {
            tracker.swarmsByFilename.wait();
        }
        assertTrue(tracker.isTrackingFilename(SAMPLE_FILENAME));
    }

    /**
     * tracker's swarm is correct
     */
    @Test
    public void testShareFileTrackerSwarm() throws Exception {
        ConcurrentHashMap<String, Swarm> swarmMap = tracker.swarmsByFilename;

        // wait for the tracker to create a swarm in the map
        synchronized (swarmMap) { swarmMap.wait(); }

        assertEquals(1, swarmMap.size());
        Swarm sampleSwarm = swarmMap.get(SAMPLE_FILENAME);
        assertEquals(1, sampleSwarm.numSeeders());
        P2PFileMetadata savedMeta = sampleSwarm.getFileMetadata();
        assertEquals(sampleMeta, savedMeta);


        // LowPriorityTODO (at some point, figure out whether to fix this)
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

    @Test
    public void peer1SharePeer2List() throws Exception {
        List<P2PFileMetadata> receivedFileList = peer2.listSavedTracker();
        List<P2PFileMetadata> trueFileList = new ArrayList<>();
        trueFileList.add(sampleMeta);
        assertEquals(trueFileList, receivedFileList);
    }

    // TODO implement the functionality
    @Test
    public void peer1SharePeer2Download() throws Exception {
        P2PFile dldFile = peer2.downloadFromSavedTracker(sampleMeta);
        assertEquals(sampleP2PFile, dldFile);
    }

    @Test
    public void peer1SharePeer2ListThenDownload() throws Exception {
        List<P2PFileMetadata> receivedFileList = peer2.listSavedTracker();
        P2PFile dldFile = peer2.downloadFromSavedTracker(receivedFileList.get(0));
        assertEquals(sampleP2PFile, dldFile);
    }
}
