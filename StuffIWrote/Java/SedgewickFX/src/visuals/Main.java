package visuals;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import util.Pair;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    static GraphicsContext gc;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello World");
        StackPane root = new StackPane();
        Scene s = new Scene(root, 400, 400, Color.BLACK);
        final Canvas canvas = new Canvas(300, 300);
        gc = canvas.getGraphicsContext2D();
        clearCanvas();
        root.getChildren().addAll(canvas);
        ObservableList<Pair<Double>> data = FXCollections.observableArrayList(
            new Pair<>(3.0, 4.0),
            new Pair<>(1.0, 3.0)
        );
        NonNegativeLineGraph lineGraph = new NonNegativeLineGraph(gc);
        lineGraph.drawBasedOn(data);
        primaryStage.setScene(s);
        primaryStage.show();
        primaryStage.show();
    }

    public static void drawStuff() {
        gc.setFill(Color.BLUE);
        gc.fillRect(10, 10, gc.getCanvas().getHeight(), gc.getCanvas().getWidth());
    }

    public static void clearCanvas() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getHeight(), gc.getCanvas().getWidth());
    }
}
