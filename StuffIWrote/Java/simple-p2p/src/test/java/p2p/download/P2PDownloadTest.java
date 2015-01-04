package p2p.download;

import org.junit.Before;
import org.junit.Test;
import p2p.BaseTest;
import p2p.exceptions.MetadataMismatchException;
import p2p.exceptions.SwarmNotFoundException;
import p2p.file.Chunk;
import p2p.file.P2PFile;
import p2p.file.P2PFileMetadata;
import p2p.peer.Peer;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class P2PDownloadTest extends BaseTest {

    P2PDownload pDownload;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        pDownload = new P2PDownload(peer, sampleMeta, peer.trkAddr);
    }


    @Test
    public void testGetSeederIPsForFile() throws Exception {
        Set<InetSocketAddress> peerIPs =
                pDownload.getSeedersForFile(sampleMeta, peer2.trkAddr);

        InetSocketAddress peerSocketAddr =
                new InetSocketAddress(peer.externalIPAddr, peer.getListeningPort());

        Set<InetSocketAddress> trueSet = new HashSet<>();
        trueSet.add(peerSocketAddr);
        assertEquals(trueSet, peerIPs);
    }

    @Test
    public void testGetSeederIPsForIncorrectDigest() throws Exception {
        thrown.expect(MetadataMismatchException.class);
        P2PFileMetadata m = sampleMeta.clone();
        m.getSha2Digest()[2] = (byte) 234;
        pDownload.getSeedersForFile(m, peer2.trkAddr);
    }

    @Test
    public void testGetSeederIPsForUnknownFilename() throws Exception {
        thrown.expect(SwarmNotFoundException.class);
        P2PFileMetadata m = sampleMeta.clone();
        m.setFilename("non-existent file");
        pDownload.getSeedersForFile(m, peer2.trkAddr);
    }

    @Test
    public void testDownloadAChunk() throws Exception {
        /* setup parameters for download */
        Set<InetSocketAddress> prs = new HashSet<>();
        prs.add(peer.getAddr());

        /* download the chunk */
        Chunk dldChunk = new ChunkDownload(sampleMeta, 0, prs).call();

        assertEquals(sampleP2PFile.getChunkNum(0), dldChunk);
    }

    @Test
    public void testSimpleFileDownload() throws Exception {
        P2PFile pFile = new P2PDownload(peer2, sampleMeta, peer2.trkAddr).call();
        assertEquals(sampleP2PFile, pFile);
    }

    @Test
    public void testBigFileDownload() throws Exception {
        P2PFile oFile = shareLargeFile();
        P2PFile dFile = new P2PDownload(peer2, oFile.metadata, peer2.trkAddr).call();
        assertEquals(oFile, dFile);
    }

    @Test
    public void testBigFileDLMultiplePeers() throws Exception {
        P2PFile oFile = shareLargeFile();
        Peer peer3 = new Peer(tracker.getInetSocketAddr());
        peer3.shareFile(VIRGINIA_FILENAME);

        P2PFile dFile = new P2PDownload(peer2, oFile.metadata, peer2.trkAddr).call();
        assertEquals(oFile, dFile);
    }
}
