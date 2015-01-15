package p2p.tracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import p2p.file.FakeP2PFile;

import java.util.ArrayList;
import java.util.Random;

/**
 * Ethan Petuchowski 1/8/15
 */
public class FakeTracker extends Tracker {
    static Random r = new Random();
    String ipPortString;

    static FakeTracker defaultFakeTracker = new FakeTracker("123.123.123.123:3300");

    static {
        defaultFakeTracker.setSwarms(
                FXCollections.observableArrayList(
                        new Swarm(FakeP2PFile.genFakeFile(), defaultFakeTracker),
                        new Swarm(FakeP2PFile.genFakeFile(), defaultFakeTracker)));
    }

    public static FakeTracker getDefaultFakeTracker() { return defaultFakeTracker; }

    public static FakeTracker makeFakeTracker() {
        String ip = r.nextInt(255)+"."+r.nextInt(255)+"."+
                    r.nextInt(255)+"."+r.nextInt(255)+":"+r.nextInt(5000);
        FakeTracker t = new FakeTracker(ip);
        ObservableList<Swarm> swarms = FXCollections.observableArrayList();
        int N = r.nextInt(6);
        for (int i = 0; i < N; i++)
            swarms.add(new Swarm(FakeP2PFile.genFakeFile(), t));
        t.setSwarms(swarms);
        return t;
    }

    public FakeTracker(String fakeIPAddrAndPort) {
        super(null, new ArrayList<>());
        ipPortString = fakeIPAddrAndPort;
    }

    @Override public String getIpPortString() { return ipPortString; }
}
