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
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class Main extends Application {
    public static void main(String[] args){launch(args);}

    @Override public void start(Stage primaryStage) {
        primaryStage.setTitle("My TreeTableView Trial");
        VBox box = new VBox();
        Scene scene = new Scene(box, 400, 300);
        scene.setFill(Color.LIGHTGREY);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

class Tracker {
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
