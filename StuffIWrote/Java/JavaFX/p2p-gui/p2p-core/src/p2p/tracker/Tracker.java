package p2p.tracker;

import p2p.file.P2PFile;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.Common;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Ethan Petuchowski 1/7/15
 */
public abstract class Tracker {
    public ObservableList<Swarm> getSwarms() { return swarms.get(); }
    public ListProperty<Swarm> swarmsProperty() { return swarms; }
    public void setSwarms(ObservableList<Swarm> swarms) { this.swarms.set(swarms); }
    public InetSocketAddress getListeningSockAddr() { return listeningSockAddr.get(); }
    public ObjectProperty<InetSocketAddress> listeningSockAddrProperty(){return listeningSockAddr;}
    public void setListeningSockAddr(InetSocketAddress addr) { this.listeningSockAddr.set(addr); }
    public String getIpPortString() { return Common.ipPortToString(getListeningSockAddr()); }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tracker)) return false;
        Tracker tracker = (Tracker) o;
        if (!listeningSockAddr.equals(tracker.listeningSockAddr)) return false;
        if (!swarms.equals(tracker.swarms)) return false;
        return true;
    }
    @Override public int hashCode() {
        int result = swarms.hashCode();
        result = 31*result+listeningSockAddr.hashCode();
        return result;
    }

    protected final ListProperty<Swarm> swarms
            = new SimpleListProperty<>(FXCollections.observableArrayList());
    protected final ObjectProperty<InetSocketAddress> listeningSockAddr;

    public Tracker(InetSocketAddress addr) {
        this.listeningSockAddr = new SimpleObjectProperty<>(addr);
    }

    public Tracker(InetSocketAddress addr, List<Swarm> swarms) {
        this(addr);
        this.swarms.addAll(swarms);
    }

    public void createSwarmFor(P2PFile pFile) {
        Swarm s = new Swarm(pFile, this);
    }
}
