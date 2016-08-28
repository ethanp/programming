package graph.visuals.linkedlist;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * 8/27/16 6:53 PM
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 500, 500, Color.BLACK);
        attachAddNodeForm(root);
        Interactivity interactive = new Interactivity(scene);
        stage.setTitle("Interactive LinkedList Application");
        stage.setScene(scene);
        stage.show();
    }

    private void attachAddNodeForm(Group root) {
        // contains button and field
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(5);

        final TextField nodeContents = new TextField();
        nodeContents.setPromptText("Enter the node content.");
        nodeContents.setPrefColumnCount(10);
        GridPane.setConstraints(nodeContents, 0, 0);
        grid.getChildren().add(nodeContents);

        Button submit = new Button("Add Node");
        GridPane.setConstraints(submit, 1, 0);
        grid.getChildren().add(submit);

        root.getChildren().add(grid);
    }
}


