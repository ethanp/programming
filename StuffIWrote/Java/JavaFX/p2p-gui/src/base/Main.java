package base;

import base.p2p.tracker.Tracker;
import base.view.TheWindowCtrl;
import base.view.panes.files.LocalFilesPaneCtrl;
import base.view.panes.trackers.TrackersPaneCtrl;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
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
            /* load Base Window */
            URL fxmlLoc = Main.class.getResource("view/TheWindow.fxml");
            FXMLLoader rootLoader = new FXMLLoader();
            rootLoader.setLocation(fxmlLoc);
            rootLayout = rootLoader.load();
            TheWindowCtrl windowCtrl = rootLoader.getController();

            /* load Trackers Pane */
            URL trackersLoc = Main.class.getResource("view/panes/trackers/TrackersPane.fxml");
            FXMLLoader trackersLoader = new FXMLLoader();
            trackersLoader.setLocation(trackersLoc);
            TitledPane trackersRoot = trackersLoader.load();
            TrackersPaneCtrl trackersCtrl = trackersLoader.getController();

            /* load LocalFiles Pane */
            URL localLoc = Main.class.getResource("view/panes/files/LocalFilesPane.fxml");
            FXMLLoader localLoader = new FXMLLoader();
            localLoader.setLocation(localLoc);
            TitledPane localViewRoot = localLoader.load();
            LocalFilesPaneCtrl localCtrl = localLoader.getController();

            /* add Panes to main Window and make them fit properly */
            windowCtrl.trackersAnchor.getChildren().add(trackersRoot);
            trackersRoot.prefWidthProperty().bind(windowCtrl.trackersAnchor.widthProperty());
            trackersRoot.prefHeightProperty().bind(windowCtrl.trackersAnchor.heightProperty());

            windowCtrl.localAnchor.getChildren().add(localViewRoot);
            localViewRoot.prefWidthProperty().bind(windowCtrl.localAnchor.widthProperty());
            localViewRoot.prefHeightProperty().bind(windowCtrl.localAnchor.heightProperty());

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("p2p-gui");
            primaryStage.show();
        }
        catch (IOException e) { e.printStackTrace(); }
    }


}
