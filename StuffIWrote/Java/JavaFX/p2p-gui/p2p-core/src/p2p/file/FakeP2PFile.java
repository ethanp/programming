package p2p.file;

import p2p.tracker.FakeTracker;
import p2p.tracker.Tracker;

import java.io.File;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ethan Petuchowski 1/8/15
 */
public class FakeP2PFile extends P2PFile {
    static Random random = new Random();
    static File containerFolder = new File("/Users/Ethan/Desktop/FakeP2PFiles");

    static {
        if (!containerFolder.exists()) {
            if (!containerFolder.mkdirs()) {
                Logger.getGlobal().log(Level.SEVERE, "couldn't create FakeP2PFile container folder");
            }
        }
    }

    public static FakeP2PFile genFakeFile() {
        File fakeFile = new File(containerFolder, "fakeFile-"+random.nextInt(500));
        long randomFilesize = random.nextInt(5_000_000);
        FakeTracker defaultFakeTracker = FakeTracker.getDefaultFakeTracker();
        return new FakeP2PFile(fakeFile, randomFilesize, defaultFakeTracker);
    }

    public FakeP2PFile(File file, long filesize, Tracker tracker) {
        super(file, filesize);
        addTracker(tracker);
        addAtMostNumFakeChunks(25);
        setPercentComplete(random.nextInt(100));
    }


    private P2PFile addAtMostNumFakeChunks(int maxChunkCount) {
        int numChunks = random.nextInt(maxChunkCount);
        for (int chunkNum = 0; chunkNum < numChunks; chunkNum++) {
            addChunk(new FakeChunk());
        }
        return this;
    }
}
