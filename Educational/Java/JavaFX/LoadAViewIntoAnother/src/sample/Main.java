package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.view.BigViewCtrl;
import sample.view.little.LittleViewCtrl;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL bigLoc = getClass().getResource("view/BigView.fxml");
        URL littleLoc = getClass().getResource("view/little/LittleView.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(bigLoc);
        BorderPane root = loader.load();
        BigViewCtrl bigViewCtrl = loader.getController();
        FXMLLoader littleLoader = new FXMLLoader();
        littleLoader.setLocation(littleLoc);
        TreeTableView<String> small = littleLoader.load();
        LittleViewCtrl littleViewCtrl = littleLoader.getController();
        bigViewCtrl.forToLoadInto.getChildren().add(small);
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Little inside Big");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
