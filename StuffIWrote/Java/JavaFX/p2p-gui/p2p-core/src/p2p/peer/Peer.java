package p2p.peer;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.net.InetSocketAddress;

/**
 * Ethan Petuchowski 1/7/15
 */
public abstract class Peer {
    private final ObjectProperty<InetSocketAddress> servingAddr;

    protected Peer(InetSocketAddress socketAddr) {
        servingAddr = new SimpleObjectProperty<>(socketAddr);
    }
}
