package base.p2p.tracker;

import java.util.Random;

/**
 * Ethan Petuchowski 1/8/15
 */
public class FakeTracker extends Tracker {
    static Random random = new Random();
    String ipAddrAndPortString;

    static FakeTracker defaultFakeTracker =
            new FakeTracker("123.123.123.123:3300");

    public static FakeTracker getDefaultFakeTracker() {
        return defaultFakeTracker;
    }

    public FakeTracker(String fakeIPAddrAndPort) {
        ipAddrAndPortString = fakeIPAddrAndPort;
    }

    @Override public String getIPAddrAndPortAsString() {
        return ipAddrAndPortString;
    }
}
