package base;

import base.p2p.file.P2PFile;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<P2PFile> localFiles = FXCollections.observableArrayList();

    @Override

    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("p2p-gui");

        initTheWindow();
    }

    private void initTheWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/TheWindow.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException e) { e.printStackTrace(); }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
