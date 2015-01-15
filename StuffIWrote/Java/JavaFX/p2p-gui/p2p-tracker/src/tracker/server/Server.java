package tracker.server;

import client.p2p.tracker.Tracker;
import javafx.beans.property.ObjectProperty;

/**
 * Ethan Petuchowski 1/14/15
 *
 * normal server.
 * not necessarily abstract, I'm just scoping it out.
 */
public abstract class Server {

    private final ObjectProperty<Tracker> tracker;

    protected Server(ObjectProperty<Tracker> tracker) {
        this.tracker = tracker;
    }

    /**
     * receive file from peer
     * look for it among swarms
     * if it exists, add peer to swarm
     * otherwise create a new swarm for it
     */
    public abstract void addFileRequest();

    /**
     * we tell a peer who wants to download a file
     * about the existing swarm info
     * so it can act accordingly
     */
    public abstract void sendSwarmInfo();
}
