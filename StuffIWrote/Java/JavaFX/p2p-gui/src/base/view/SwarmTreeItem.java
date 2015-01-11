package base.view;

import base.p2p.tracker.Swarm;
import base.p2p.tracker.Tracker;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Ethan Petuchowski 1/11/15
 */
public class SwarmTreeItem {
    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public void setName(String name) { this.name.set(name); }

    // TODO the rest of these should have Setters/Getters

    protected final ListProperty<Swarm> swarms;
    protected final StringProperty name;
    protected final LongProperty size;
    protected final IntegerProperty numSeeders;
    protected final IntegerProperty numLeechers;

    // TODO I guess all these fields have to be WATCHING their corresponding source value
    // because otherwise they will become out-of-date

    public SwarmTreeItem(Swarm swarm) {
        swarms = null;
        name = swarm.getP2pFile().filenameProperty();
        size = new SimpleLongProperty(swarm.getP2pFile().getFilesizeBytes());
        numSeeders = new SimpleIntegerProperty(swarm.getLeechers().size());
        numLeechers = new SimpleIntegerProperty(swarm.getSeeders().size());
    }

    public SwarmTreeItem(Tracker tracker) {
        swarms = tracker.swarmsProperty();
        name = new SimpleStringProperty(tracker.getIpPortString());
        size = new SimpleLongProperty(tracker.swarmsProperty().size());
        numSeeders = null;
        numLeechers = null;
    }
}
