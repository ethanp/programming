package base;

import base.p2p.tracker.Tracker;
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
    public Main() { /* I could put stuff in here, but at this point there is no need */ }
    public static void main(String[] args) { launch(args); }
    public static Stage getPrimaryStage() { return primaryStage; }
    private static Stage primaryStage;
    private BorderPane rootLayout;

    /** this is the list of all trackers known to the app */
    public static ObservableList<Tracker> knownTrackers = FXCollections.observableArrayList();

    @Override public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;
        Main.primaryStage.setTitle("p2p-gui");
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
