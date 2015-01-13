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
    public Image getIcon() { return icon; }
    public void setIcon(Image icon) { this.icon = icon; }
    public Tracker getTracker() { return tracker; }
    public Swarm getSwarm() { return swarm; }


    private final boolean root;
    private final Tracker tracker;


    private final Swarm swarm;

    public boolean isRoot()     { return root; }
    public boolean isTracker()  { return tracker != null; }
    public boolean isSwarm()     { return swarm != null; }

    private Image icon;

    private final StringProperty name;
    private final StringProperty size;
    private final StringProperty numSeeders;
    private final StringProperty numLeechers;

    public Celery(Root r) {
        root = true;
        tracker = null;
        swarm = null;
        icon = Main.zoomIcon;
        name = new SimpleStringProperty("Trackers:");
        size = new SimpleStringProperty("");
        numLeechers = new SimpleStringProperty("");
        numSeeders = new SimpleStringProperty("");
    }

    public Celery(Tracker tracker) {
        root = false;
        this.tracker = tracker;
        swarm = null;
        icon = Main.zipIcon;
        name = new SimpleStringProperty("trk: "+tracker.getLoc());
        size = new SimpleStringProperty(tracker.getSwarms().size()+" f");
        numLeechers = new SimpleStringProperty("");
        numSeeders = new SimpleStringProperty("");
    }

    public Celery(Swarm swarm) {
        root = false;
        tracker = null;
        this.swarm = swarm;
        icon = Main.zoomIcon;
        name = new SimpleStringProperty(swarm.getFile().getName());
        size = new SimpleStringProperty(swarm.getFile().getSize()+" B");
        numLeechers = new SimpleStringProperty(swarm.getLeechers().size()+"");
        numSeeders = new SimpleStringProperty(swarm.getSeeders().size()+"");
    }
}
