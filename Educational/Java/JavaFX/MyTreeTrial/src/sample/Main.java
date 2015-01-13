package sample;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class Main extends Application {
    public static void main(String[] args){launch(args);}
    public static Image zipIcon = new Image(Main.class.getResourceAsStream("zip.png"));
    public static Image zoomIcon = new Image(Main.class.getResourceAsStream("zoom.png"));
    TreeItem<Celery> rootNode = new TreeItem<>(new Celery(new Root()));

    @Override public void start(Stage primaryStage) {
        primaryStage.setTitle("My TreeTableView Trial");
        VBox box = new VBox();
        Scene scene = new Scene(box, 400, 300);
        scene.setFill(Color.LIGHTGREY);

        TreeTableColumn<Celery, String> nameCol = new TreeTableColumn<>("Name");
        TreeTableColumn<Celery, String> sizeCol = new TreeTableColumn<>("Size");
        TreeTableColumn<Celery, String> seedersCol = new TreeTableColumn<>("#Seeders");
        TreeTableColumn<Celery, String> leechersCol = new TreeTableColumn<>("#Leechers");

        nameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        sizeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("size"));
        seedersCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("numSeeders"));
        leechersCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("numLeechers"));

        nameCol.setPrefWidth(85);

        TreeTableView<Celery> tree = new TreeTableView<>(rootNode);
        tree.setEditable(false);
        tree.getColumns().addAll(nameCol, sizeCol, seedersCol, leechersCol);

        Tracker tr1 = new Tracker(3);
        Celery cTr1 = new Celery(tr1);
        rootNode.getChildren().add(new TreeItem<>(cTr1));
        rootNode.setExpanded(true);

        box.getChildren().add(tree);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

class CeleryCell extends TreeCell<Celery> {
    private TextField textField;
    private ContextMenu menu = new ContextMenu();
    
}

class Tracker {
    public int getLoc() { return loc.get(); }
    public IntegerProperty locProperty() { return loc; }
    public void setLoc(int loc) { this.loc.set(loc); }
    public ObservableList<Swarm> getSwarms() { return swarms.get(); }
    public ListProperty<Swarm> swarmsProperty() { return swarms; }
    public void setSwarms(ObservableList<Swarm> swarms) { this.swarms.set(swarms); }

    private final IntegerProperty loc;
    private final ListProperty<Swarm> swarms;
    private static int ctr = 1;
    public Tracker(int numFiles) {
        loc = new SimpleIntegerProperty(Util.nextInt(100));
        swarms = new SimpleListProperty<>(FXCollections.observableArrayList());
        for (int i = 0; i < numFiles; i++)
            swarms.add(new Swarm("file-"+ctr++, this));
    }
}

class Swarm {
    public P2PFile getFile() { return file.get(); }
    public ObjectProperty<P2PFile> fileProperty() { return file; }
    public void setFile(P2PFile file) { this.file.set(file); }
    public Tracker getTracker() { return tracker.get(); }
    public ObjectProperty<Tracker> trackerProperty() { return tracker; }
    public void setTracker(Tracker tracker) { this.tracker.set(tracker); }
    public ObservableList<Peer> getSeeders() { return seeders.get(); }
    public ListProperty<Peer> seedersProperty() { return seeders; }
    public void setSeeders(ObservableList<Peer> seeders) { this.seeders.set(seeders); }
    public ObservableList<Peer> getLeechers() { return leechers.get(); }
    public ListProperty<Peer> leechersProperty() { return leechers; }
    public void setLeechers(ObservableList<Peer> leechers) { this.leechers.set(leechers); }

    private final ObjectProperty<P2PFile> file;
    private final ObjectProperty<Tracker> tracker;
    private final ListProperty<Peer> seeders;
    private final ListProperty<Peer> leechers;
    public Swarm(String filename, Tracker trkr) {
        file = new SimpleObjectProperty<>(new P2PFile(filename));
        tracker = new SimpleObjectProperty<>(trkr);
        seeders = new SimpleListProperty<>(FXCollections.observableArrayList());
        leechers = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
}

class P2PFile {
    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public void setName(String name) { this.name.set(name); }
    public long getSize() { return size.get(); }
    public LongProperty sizeProperty() { return size; }
    public void setSize(long size) { this.size.set(size); }
    public ObservableList<Tracker> getKnownTrackers() { return knownTrackers.get(); }
    public ListProperty<Tracker> knownTrackersProperty() { return knownTrackers; }
    public void setKnownTrackers(ObservableList<Tracker> knownTrackers) { this.knownTrackers.set(knownTrackers); }

    private final StringProperty name;
    private final LongProperty size;
    private final ListProperty<Tracker> knownTrackers;
    public P2PFile(String filename) {
        name = new SimpleStringProperty(filename);
        size = new SimpleLongProperty(Util.nextInt(1000));
        knownTrackers = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
}

class Peer {
    private final IntegerProperty loc;
    public Peer() {
        loc = new SimpleIntegerProperty(Util.nextInt(1000));
    }
}

class Util {
    static Random random = new Random();
    static int nextInt(int bound) { return random.nextInt(bound); }
}

class Root {}
