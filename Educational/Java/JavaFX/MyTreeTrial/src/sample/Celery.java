package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class Celery {
    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public void setName(String name) { this.name.set(name); }
    public String getSize() { return size.get(); }
    public StringProperty sizeProperty() { return size; }
    public void setSize(String size) { this.size.set(size); }
    public String getNumSeeders() { return numSeeders.get(); }
    public StringProperty numSeedersProperty() { return numSeeders; }
    public void setNumSeeders(String numSeeders) { this.numSeeders.set(numSeeders); }
    public String getNumLeechers() { return numLeechers.get(); }
    public StringProperty numLeechersProperty() { return numLeechers; }
    public void setNumLeechers(String numLeechers) { this.numLeechers.set(numLeechers); }

    enum Type { ROOT, TRACKER, FILE }
    private Type type;
    public boolean isRoot()     { return type == Type.ROOT; }
    public boolean isTracker()  { return type == Type.TRACKER; }
    public boolean isFile()     { return type == Type.FILE; }
    private Image icon;
    private final StringProperty name;
    private final StringProperty size;
    private final StringProperty numSeeders;
    private final StringProperty numLeechers;
    public Celery(Root r) {
        type = Type.ROOT;
        icon = Main.zoomIcon;
        name = new SimpleStringProperty("Trackers:");
        size = new SimpleStringProperty("");
        numLeechers = new SimpleStringProperty("");
        numSeeders = new SimpleStringProperty("");
    }
    public Celery(Tracker tracker) {
        type = Type.TRACKER;
        icon = Main.zipIcon;
        name = new SimpleStringProperty("trk: "+tracker.getLoc());
        size = new SimpleStringProperty(tracker.getSwarms().size()+" f");
        numLeechers = new SimpleStringProperty("");
        numSeeders = new SimpleStringProperty("");
    }
    public Celery(Swarm swarm) {
        type = Type.FILE;
        icon = Main.zoomIcon;
        name = new SimpleStringProperty(swarm.getFile().getName());
        size = new SimpleStringProperty(swarm.getFile().getSize()+" B");
        numLeechers = new SimpleStringProperty(swarm.getLeechers().size()+"");
        numSeeders = new SimpleStringProperty(swarm.getSeeders().size()+"");
    }
}
