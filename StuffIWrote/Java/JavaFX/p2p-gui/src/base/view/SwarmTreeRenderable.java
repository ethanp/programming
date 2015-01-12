package base.view;

import base.p2p.tracker.Swarm;
import base.p2p.tracker.Tracker;
import base.util.TreeTableRoot;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Ethan Petuchowski 1/11/15
 */
public class SwarmTreeRenderable {

    /* getters and setters */
    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public void setName(String name) { this.name.set(name); }
    public ObservableList<Swarm> getSwarms() { return swarms.get(); }
    public ListProperty<Swarm> swarmsProperty() { return swarms; }
    public void setSwarms(ObservableList<Swarm> swarms) { this.swarms.set(swarms); }
    public long getSize() { return size.get(); }
    public LongProperty sizeProperty() { return size; }
    public void setSize(long size) { this.size.set(size); }
    public int getNumSeeders() { return numSeeders.get(); }
    public IntegerProperty numSeedersProperty() { return numSeeders; }
    public void setNumSeeders(int numSeeders) { this.numSeeders.set(numSeeders); }
    public int getNumLeechers() { return numLeechers.get(); }
    public IntegerProperty numLeechersProperty() { return numLeechers; }
    public void setNumLeechers(int numLeechers) { this.numLeechers.set(numLeechers); }
    public ObservableList<Tracker> getKnownTrackers() { return knownTrackers.get(); }
    public ListProperty<Tracker> knownTrackersProperty() { return knownTrackers; }
    public void setKnownTrackers(ObservableList<Tracker> knownTrackers) {
        this.knownTrackers.set(knownTrackers);
    }

    /* properties */
    protected final StringProperty name;
    protected final LongProperty size;
    protected final IntegerProperty numSeeders;
    protected final IntegerProperty numLeechers;
    protected final ListProperty<Swarm> swarms;
    protected final ListProperty<Tracker> knownTrackers;

    // TODO I guess all these fields have to be WATCHING their corresponding source value
    // because otherwise they may become out-of-date

    /* wrapping constructors */
    public SwarmTreeRenderable(Swarm swarm) {
        swarms = null;
        name = swarm.getP2pFile().filenameProperty();
        size = new SimpleLongProperty(swarm.getP2pFile().getFilesizeBytes());
        numSeeders = new SimpleIntegerProperty(swarm.getLeechers().size());
        numLeechers = new SimpleIntegerProperty(swarm.getSeeders().size());
        knownTrackers = null;
    }

    public SwarmTreeRenderable(Tracker tracker) {
        swarms = tracker.swarmsProperty();
        name = new SimpleStringProperty(tracker.getIpPortString());
        size = new SimpleLongProperty(tracker.swarmsProperty().size());
        numSeeders = null;
        numLeechers = null;
        knownTrackers = null;
    }

    /** create a "base" SwarmTreeItem used to list
     *  the known Trackers as its children */
    public SwarmTreeRenderable(TreeTableRoot o) {
        swarms = null;
        name = new SimpleStringProperty("known Trackers");
        knownTrackers = new SimpleListProperty<>(FXCollections.observableArrayList());
        size = new SimpleLongProperty(knownTrackers.size());
        numSeeders = null;
        numLeechers = null;
    }

    public SwarmTreeRenderable addTracker(Tracker tracker) {
        if (knownTrackers == null) throw new NullPointerException("this is not a tracker item");
        knownTrackers.add(tracker);
        return this;
    }

    public SwarmTreeRenderable addAllTrackers(Tracker... trackers) {
        if (knownTrackers == null) throw new NullPointerException("this is not a tracker item");
        knownTrackers.addAll(trackers);
        return this;
    }
}
