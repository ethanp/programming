package graph.visuals.app;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Ethan Petuchowski 6/5/16
 */
class Interactive {
    private final Group root;

    Interactive(Scene scene) {
        this.root = (Group) scene.getRoot();
        scene.setOnMouseClicked(e ->
            addNodeAt(new Point2D(e.getX(), e.getY()))
        );
    }

    void addNodeAt(Point2D point) {
        Circle circle = new Circle(point.getX(), point.getY(), 25);
        circle.setFill(Color.BLUE);
        root.getChildren().add(circle);
    }

    void exampleAnimation() {
        TranslateTransition translate = new TranslateTransition(Duration.millis(750));
        translate.setToX(390);
        translate.setToY(390);

        FillTransition fill = new FillTransition(Duration.millis(750));
        fill.setToValue(Color.RED);

        ScaleTransition scale = new ScaleTransition(Duration.millis(750));
        scale.setToX(0.1);
        scale.setToY(0.1);

        Circle circle = new Circle(0, 0, 5);
        ParallelTransition transition = new ParallelTransition(circle, translate, fill, scale);
        transition.setCycleCount(Timeline.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();
    }
}
