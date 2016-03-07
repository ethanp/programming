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

public class Main extends Application {

    static GraphicsContext gc;

    public static void main(String[] args) {
        launch(args);
    }

    public static void drawStuff() {
        gc.setFill(Color.BLUE);
        gc.fillRect(10, 10, gc.getCanvas().getHeight(), gc.getCanvas().getWidth());
    }

    public static void clearCanvas() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getHeight(), gc.getCanvas().getWidth());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello World");
        StackPane root = new StackPane();
        final int HEIGHT = 700;
        final int WIDTH = 700;
        Scene s = new Scene(root, WIDTH, HEIGHT, Color.BLACK);
        final Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        clearCanvas();
        root.getChildren().addAll(canvas);
        ObservableList<Double> data = FXCollections.observableArrayList(
            4.0,
            3.0, 5.0, 2.0
        );
        NonNegativeBarGraph lineGraph = new NonNegativeBarGraph(gc);
        lineGraph.drawBasedOn(data);
        primaryStage.setScene(s);
        primaryStage.show();
        primaryStage.show();
    }
}
