package p2p.download;

import org.junit.Before;
import org.junit.Test;
import p2p.BaseTest;
import p2p.exceptions.MetadataMismatchException;
import p2p.exceptions.SwarmNotFoundException;
import p2p.file.Chunk;
import p2p.file.P2PFileMetadata;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class P2PDownloadTest extends BaseTest {

    P2PDownload pDownload;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        pDownload = new P2PDownload(peer, sampleP2PFile.metadata, peer.trkAddr);
    }


    @Test
    public void testGetSeederIPsForFile() throws Exception {
        Set<InetSocketAddress> peerIPs =
                pDownload.getSeedersForFile(sampleP2PFile.metadata, peer2.trkAddr);

        InetSocketAddress peerSocketAddr =
                new InetSocketAddress(peer.externalIPAddr, peer.getListeningPort());

        Set<InetSocketAddress> trueSet = new HashSet<>();
        trueSet.add(peerSocketAddr);
        assertEquals(trueSet, peerIPs);
    }

    @Test
    public void testGetSeederIPsForIncorrectDigest() throws Exception {
        thrown.expect(MetadataMismatchException.class);
        P2PFileMetadata m = sampleP2PFile.metadata.clone();
        m.getSha2Digest()[2] = (byte) 234;
        pDownload.getSeedersForFile(m, peer2.trkAddr);
    }

    @Test
    public void testGetSeederIPsForUnknownFilename() throws Exception {
        thrown.expect(SwarmNotFoundException.class);
        P2PFileMetadata m = sampleP2PFile.metadata.clone();
        m.setFilename("non-existent file");
        pDownload.getSeedersForFile(m, peer2.trkAddr);
    }

    @Test
    public void testDownloadAChunk() throws Exception {

        /* setup parameters for download */
        P2PFileMetadata meta = sampleP2PFile.metadata;
        InetSocketAddress hostAddr =
                new InetSocketAddress(peer.externalIPAddr, peer.getListeningPort());
        Set<InetSocketAddress> prs = new HashSet<>();
        prs.add(hostAddr);

        /* download the chunk */
        Chunk dldChunk = new ChunkDownload(meta, 0, prs).call();

        assertEquals(sampleP2PFile.getChunkNum(0), dldChunk);
    }
}
