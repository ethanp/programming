package p2p.peer;

import org.junit.Test;
import p2p.BaseTest;
import p2p.Common;
import p2p.Swarm;
import p2p.Tracker;
import p2p.file.P2PFile;
import p2p.file.P2PFileMetadata;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
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
        /* I'm not sure why the listing seems to now be "ordered before" this wait and it used to not?
         * But maybe I should be using a ConditionVariable instead of wait() because the
         * situation I'm having is that the file gets listed and we get notify()d before
         * we begin to wait() here, so if we wait(), we wait for ever because the notify()cation
         * already passed us up
         */
//        synchronized (tracker.swarmsByFilename) { tracker.swarmsByFilename.wait(); }
        assertTrue(tracker.isTrackingFilename(SAMPLE_FILENAME));
    }

    /**
     * tracker's swarm is correct
     */
    @Test
    public void testShareFileTrackerSwarm() throws Exception {
        ConcurrentHashMap<String, Swarm> swarmMap = tracker.swarmsByFilename;
//        synchronized (swarmMap) { swarmMap.wait(); } /* see above for note about synchronization*/
        assertEquals(1, swarmMap.size());

        Swarm sampleSwarm = swarmMap.get(SAMPLE_FILENAME);
        assertEquals(1, sampleSwarm.numSeeders());
        assertEquals(sampleMeta, sampleSwarm.getFileMetadata());

        InetAddress myExternalIPAddr = Common.findMyIP();
        Iterator<InetSocketAddress> it = sampleSwarm.getSeeders().iterator();
        InetSocketAddress savedSockAddrOfPeerInSwarm = it.next();
        assertEquals(myExternalIPAddr, savedSockAddrOfPeerInSwarm.getAddress());
    }

    @Test
    public void peer1SharePeer2List() throws Exception {
        List<P2PFileMetadata> receivedFileList = peer2.listSavedTracker();
        List<P2PFileMetadata> trueFileList = new ArrayList<>();
        trueFileList.add(sampleMeta);
        assertEquals(trueFileList, receivedFileList);
    }

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

    @Test
    public void listEmptyTracker() throws Exception {
        Tracker tracker1 = new Tracker();
        tracker1.start();
        List<P2PFileMetadata> list = peer.listTracker(tracker1.getInetSocketAddr());
        assertTrue(list.isEmpty());
    }
}
