package base.p2p.tracker;

import base.p2p.file.P2PFile;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

/**
 * Ethan Petuchowski 1/7/15
 */
public abstract class Tracker {

    public abstract String getSocketAsString();

    protected final ListProperty<P2PFile> p2pFiles;
    public ObservableList<P2PFile> getp2pFiles() { return p2pFiles.get(); }
    public ListProperty<P2PFile> p2pFilesProperty() { return p2pFiles; }
    public void setp2pFiles(ObservableList<P2PFile> p2pFiles) { this.p2pFiles.set(p2pFiles); }
    public void setp2pFiles(List<P2PFile> p2pFiles) {
        setp2pFiles(FXCollections.observableArrayList(p2pFiles));
    }
    public void setp2pFiles(P2PFile... p2pFiles) { setp2pFiles(Arrays.asList(p2pFiles)); }

    protected final ObjectProperty<InetSocketAddress> listeningSockAddr;
    public InetSocketAddress getListeningSockAddr() { return listeningSockAddr.get(); }
    public ObjectProperty<InetSocketAddress> listeningSockAddrProperty(){return listeningSockAddr;}
    public void setListeningSockAddr(InetSocketAddress addr) { this.listeningSockAddr.set(addr); }

    public Tracker(List<P2PFile> p2pFiles, InetSocketAddress addr) {
        this.p2pFiles = new SimpleListProperty<>(FXCollections.observableArrayList(p2pFiles));
        this.listeningSockAddr = new SimpleObjectProperty<>(addr);
    }
}
