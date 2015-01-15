package tracker.app;

import javafx.beans.property.ObjectProperty;
import p2p.tracker.Tracker;
import tracker.server.Server;

/**
 * Ethan Petuchowski 1/15/15
 */
public abstract class AppState {
    public Tracker getTracker() { return tracker.get(); }
    public ObjectProperty<Tracker> trackerProperty() { return tracker; }
    public void setTracker(Tracker tracker) { this.tracker.set(tracker); }

    protected final ObjectProperty<Tracker> tracker;
    protected final ObjectProperty<Server> server;

    protected AppState() {
        tracker = null;
        server = null;
    }
}
