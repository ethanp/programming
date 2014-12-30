package p2p;

import org.junit.Test;

public class PeerTest {

    @Test
    public void testShareFile() throws Exception {
        Peer pier1 = new Peer();
        Tracker tracker = new Tracker();
        tracker.start();
        pier1.setTracker(tracker.getInetSocketAddr());
        pier1.shareFile("asdfile");
    }
}
