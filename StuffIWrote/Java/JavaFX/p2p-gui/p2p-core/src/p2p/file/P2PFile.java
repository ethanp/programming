package p2p.file;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import p2p.tracker.Tracker;
import p2p.transfer.Transfer;
import util.Common;

import java.io.File;
import java.nio.file.Path;

/**
 * Ethan Petuchowski 1/7/15
 */
public abstract class P2PFile {

    // TODO maybe the filename & File object should be listening to each other
    protected final ObjectProperty<File> localFile;
    public File getLocalFile() { return localFile.get(); }
    public ObjectProperty<File> localFileProperty() { return localFile; }
    public void setLocalFile(File localFile) { this.localFile.set(localFile); }

    protected final StringProperty filename;
    public String getFilename() { return filename.get(); }
    public StringProperty filenameProperty() { return filename; }
    public void setFilename(String filename) { this.filename.set(filename); }


    protected final ListProperty<Tracker> knownTrackers;
    public ObservableList<Tracker> getKnownTrackers() { return knownTrackers.get(); }
    public ListProperty<Tracker> knownTrackersProperty() { return knownTrackers; }
    public void setKnownTrackers(ObservableList<Tracker> knownTrackers) {
        this.knownTrackers.set(knownTrackers);
    }

    protected final ListProperty<Chunk> chunks;
    public ObservableList<Chunk> getChunks() { return chunks.get(); }
    public ListProperty<Chunk> chunksProperty() { return chunks; }
    public void setChunks(ObservableList<Chunk> chunks) { this.chunks.set(chunks); }

    protected final ListProperty<Transfer> ongoingTransfers;
    public ObservableList<Transfer> getOngoingTransfers() { return ongoingTransfers.get(); }
    public ListProperty<Transfer> ongoingTransfersProperty() { return ongoingTransfers; }
    public void setOngoingTransfers(ObservableList<Transfer> ongoingTransfers) {
        this.ongoingTransfers.set(ongoingTransfers);
    }

    protected final IntegerProperty percentComplete;
    public int getPercentComplete() { return percentComplete.get(); }
    public IntegerProperty percentCompleteProperty() { return percentComplete; }
    public void setPercentComplete(int percentComplete){this.percentComplete.set(percentComplete);}
    public String getCompletenessString() { return getPercentComplete()+"%"; }

    protected final LongProperty filesizeBytes;
    public long getFilesizeBytes() { return filesizeBytes.get(); }
    public LongProperty filesizeBytesProperty() { return filesizeBytes; }
    public void setFilesizeBytes(long filesizeBytes) { this.filesizeBytes.set(filesizeBytes); }
    public String getFilesizeString() {return Common.formatByteCountToString(getFilesizeBytes());}

    public P2PFile(File baseFile, long filesize)
    {
        localFile = new SimpleObjectProperty<>(baseFile);
        filename = new SimpleStringProperty(baseFile.getName());
        knownTrackers = new SimpleListProperty<>(FXCollections.<Tracker>observableArrayList());
        chunks = new SimpleListProperty<>(FXCollections.<Chunk>observableArrayList());
        ongoingTransfers = new SimpleListProperty<>(FXCollections.<Transfer>observableArrayList());
        filesizeBytes = new SimpleLongProperty(filesize);
        percentComplete = new SimpleIntegerProperty(0);
    }

    public P2PFile(Path filePath) { this(filePath.toFile(), filePath.toFile().length()); }

    public P2PFile addTracker(Tracker tracker) {
        knownTrackers.add(tracker);
        return this;
    }

    // 'protected' means accessible from: this class, package, subclass; no one else
    protected P2PFile addChunk(Chunk chunk) {
        chunks.add(chunk);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof P2PFile)) return false;
        P2PFile file = (P2PFile) o;
        if (!chunks.equals(file.chunks)) return false;
        if (!filename.equals(file.filename)) return false;
        if (!filesizeBytes.equals(file.filesizeBytes)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = filename.hashCode();
        result = 31*result+chunks.hashCode();
        result = 31*result+filesizeBytes.hashCode();
        return result;
    }
}
