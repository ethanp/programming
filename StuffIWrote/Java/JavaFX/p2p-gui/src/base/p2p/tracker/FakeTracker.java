package base.p2p.tracker;

import base.p2p.file.FakeP2PFile;
import javafx.collections.FXCollections;

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
        defaultFakeTracker.setSwarms(
                FXCollections.observableArrayList(
                        new Swarm(FakeP2PFile.genFakeFile()),
                        new Swarm(FakeP2PFile.genFakeFile())));
    }

    public static FakeTracker getDefaultFakeTracker() { return defaultFakeTracker; }

    public FakeTracker(String fakeIPAddrAndPort) {
        super(new ArrayList<>(), null);
        ipPortString = fakeIPAddrAndPort;
    }

    @Override public String getIpPortString() { return ipPortString; }
}
