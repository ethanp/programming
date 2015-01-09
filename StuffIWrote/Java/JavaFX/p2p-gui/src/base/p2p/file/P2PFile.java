package base.p2p.file;

import base.p2p.tracker.Tracker;
import base.p2p.transfer.Transfer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.nio.file.Path;

/**
 * Ethan Petuchowski 1/7/15
 */
public abstract class P2PFile {

    // TODO maybe the filename & File object should be listening to each other
    private final ObjectProperty<File> localFile;
    private final StringProperty filename;
    private final ListProperty<Tracker> trackers;
    private final ListProperty<Chunk> chunks;
    private final ListProperty<Transfer> ongoingTransfers;
    private final IntegerProperty filesizeBytes;

    public P2PFile(File baseFile)
    {
        localFile = new SimpleObjectProperty<File>(baseFile);
        filename = new SimpleStringProperty(localFile.getName());
        trackers = new SimpleListProperty<Tracker>();
        chunks = new SimpleListProperty<Chunk>();
        ongoingTransfers = new SimpleListProperty<Transfer>();
        filesizeBytes = new SimpleIntegerProperty(-1);
    }
    public P2PFile(Path filePath) {
        this(filePath.toFile());
    }

    public abstract int getSize();

    public P2PFile addTracker(Tracker tracker) {
        trackers.add(tracker);
        return this;
    }

    // 'protected' means accessible from: this class, package, subclass; no one else
    protected P2PFile addChunk(Chunk chunk) {
        chunks.add(chunk);
        return this;
    }
}
