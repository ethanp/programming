package base.p2p.tracker;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    public abstract String getIpPortString();

    protected final ListProperty<Swarm> swarms;
    protected final ObjectProperty<InetSocketAddress> listeningSockAddr;


    public Tracker(List<Swarm> swarms, InetSocketAddress addr) {
        this.swarms = new SimpleListProperty<>(FXCollections.observableArrayList(swarms));
        this.listeningSockAddr = new SimpleObjectProperty<>(addr);
    }
}
