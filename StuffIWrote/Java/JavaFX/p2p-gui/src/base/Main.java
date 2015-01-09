package base;

import base.p2p.file.P2PFile;
import base.p2p.tracker.Tracker;
import base.p2p.transfer.Transfer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    public static void main(String[] args) { launch(args); }
    public Stage getPrimaryStage() { return primaryStage; }

    private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<P2PFile> localFiles = FXCollections.observableArrayList();
    private ObservableList<Tracker> knownTrackers = FXCollections.observableArrayList();
    private ObservableList<Transfer> ongoingTransfers = FXCollections.observableArrayList();

    /* TODO add sample data */
    public Main() {
    }

    @Override public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("p2p-gui");

        loadTheWindow();
    }

    private void loadTheWindow() {
        try {
            URL fxmlLoc = Main.class.getResource("view/TheWindow.fxml");
            rootLayout = FXMLLoader.load(fxmlLoc);
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("p2p-gui");
            primaryStage.show();
        }
        catch (IOException e) { e.printStackTrace(); }
    }


}
