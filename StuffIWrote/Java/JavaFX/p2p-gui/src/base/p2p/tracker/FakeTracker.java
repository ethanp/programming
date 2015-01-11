package base.p2p.tracker;

import base.p2p.file.FakeP2PFile;

import java.util.ArrayList;
import java.util.Random;

/**
 * Ethan Petuchowski 1/8/15
 */
public class FakeTracker extends Tracker {
    static Random random = new Random();
    String ipPortString;

    static FakeTracker defaultFakeTracker = new FakeTracker("123.123.123.123:3300");

    static {
        defaultFakeTracker.setp2pFiles(FakeP2PFile.genFakeFile(), FakeP2PFile.genFakeFile());
    }

    public static FakeTracker getDefaultFakeTracker() { return defaultFakeTracker; }

    public FakeTracker(String fakeIPAddrAndPort) {
        super(new ArrayList<>(), null);
        ipPortString = fakeIPAddrAndPort;
    }

    @Override public String getIpPortString() { return ipPortString; }
}
