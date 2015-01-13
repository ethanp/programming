package sample;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class Main extends Application {
    public static void main(String[] args){launch(args);}
    public static Image zipIcon = new Image(Main.class.getResourceAsStream("zip.png"));
    public static Image zoomIcon = new Image(Main.class.getResourceAsStream("zoom.png"));
    static public ObservableList<Tracker> knownTrackers = FXCollections.observableArrayList();
    CeleryItem rootNode = new CeleryItem(new Celery(new Root()));
    static Stage primary;

    @Override public void start(Stage primaryStage) {
        primary = primaryStage;
        knownTrackers.addAll(new Tracker(3), new Tracker(5), new Tracker(1));

        rootNode.setExpanded(true);
        primary.setTitle("My TreeTableView Trial");
        VBox box = new VBox();
        Scene scene = new Scene(box, 400, 300);
        scene.setFill(Color.LIGHTGREY);

        TreeTableColumn<Celery, Celery> nameCol = new TreeTableColumn<>("Name");
        TreeTableColumn<Celery, Celery> sizeCol = new TreeTableColumn<>("Size");
        TreeTableColumn<Celery, Celery> seedersCol = new TreeTableColumn<>("#Seeders");
        TreeTableColumn<Celery, Celery> leechersCol = new TreeTableColumn<>("#Leechers");

        nameCol.setPrefWidth(110);

        nameCol.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getValue()));
        sizeCol.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getValue()));
        seedersCol.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getValue()));
        leechersCol.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getValue()));

        nameCol.setCellFactory(e -> new CeleryCell(CeleryCell.Cols.NAME));
        sizeCol.setCellFactory(e -> new CeleryCell(CeleryCell.Cols.SIZE));
        seedersCol.setCellFactory(e -> new CeleryCell(CeleryCell.Cols.NUM_SEEDERS));
        leechersCol.setCellFactory(e -> new CeleryCell(CeleryCell.Cols.NUM_LEECHERS));

        primary.setScene(scene);

        TreeTableView<Celery> tree = new TreeTableView<>(rootNode);
        tree.setEditable(false);
        tree.getColumns().addAll(nameCol, sizeCol, seedersCol, leechersCol);

        box.getChildren().add(tree);

        primary.show();
    }
}

class CeleryItem extends TreeItem<Celery> {

    private boolean childrenKnown = false;

    public CeleryItem(Celery item) { super(item); }

    @Override public boolean isLeaf() { return getValue().isSwarm(); }

    @Override public ObservableList<TreeItem<Celery>> getChildren() {
        if (!childrenKnown) {
            childrenKnown = true;
            if (getValue().isTracker())
                for (Swarm swarm : getValue().getTracker().getSwarms())
                    super.getChildren().add(new CeleryItem(new Celery(swarm)));
            else if (getValue().isRoot())
                for (Tracker tracker : Main.knownTrackers)
                    super.getChildren().add(new CeleryItem(new Celery(tracker)));
        }
        return super.getChildren();
    }
}

class CeleryCell extends TreeTableCell<Celery, Celery> {
    static enum Cols { NAME, SIZE, NUM_SEEDERS, NUM_LEECHERS }

    private Cols col;
    private ContextMenu contextMenu = new ContextMenu();

    private String myTxt() {
        switch (col) {
            case NAME:          return getItem().getName();
            case SIZE:          return getItem().getSize();
            case NUM_SEEDERS:   return getItem().getNumSeeders();
            case NUM_LEECHERS:  return getItem().getNumLeechers();
        }
        throw new RuntimeException("unreachable");
    }
    public CeleryCell(Cols c) {
        col = c;
        MenuItem addTracker = new MenuItem("Add tracker to file");
        MenuItem remove = new MenuItem("Remove file from list");
        contextMenu.getItems().addAll(addTracker, remove);

        addTracker.setOnAction(event -> {});    // TODO
        remove.setOnAction(event -> {});        // TODO

        addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            if (event.getButton() == MouseButton.SECONDARY && getItem().isSwarm()) {
                event.consume();
                contextMenu.show(Main.primary, event.getScreenX(), event.getScreenY());
            }
        });
    }
    @Override protected void updateItem(Celery item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) { setText(null); setGraphic(null); return; }
        if (col == Cols.NAME && !getItem().isSwarm())
            setGraphic(new ImageView(getItem().getIcon()));
        setText(myTxt());
    }
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
    public void setKnownTrackers(ObservableList<Tracker> knownTrackers) {
        this.knownTrackers.set(knownTrackers);
    }

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
