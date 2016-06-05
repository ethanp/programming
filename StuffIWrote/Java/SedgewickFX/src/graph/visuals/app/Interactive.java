package graph.visuals.app;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;

/**
 * Ethan Petuchowski 6/5/16
 */
class Interactive {
    private final Group root;

    private Circle dragSource = null;

    Interactive(Scene scene) {
        this.root = (Group) scene.getRoot();
        scene.setOnMouseClicked(click -> {
            if (!isDragging()) addNodeAt(new Point2D(click.getX(), click.getY()));
        });
        scene.setOnDragDone(event -> {
            if (isDragging()) {
                System.out.println("cancelling edge creation");
                dragSource.setFill(Color.BLUE);
                dragSource = null;
            }
        });
    }

    private boolean isDragging() {
        return dragSource != null;
    }

    void addNodeAt(Point2D point) {
        Circle circle = new Circle(point.getX(), point.getY(), 25);
        circle.setFill(Color.BLUE);
        circle.setOnDragDetected(event -> {
            System.out.println("starting edge creation");
            if (!isDragging()) {
                ClipboardContent content = new ClipboardContent();
                content.putString("circle drag"); // this is REQUIRED (and useless!)
                circle.startDragAndDrop(TransferMode.ANY).setContent(content);
                circle.setFill(Color.GREEN);
                dragSource = circle;
            }
            else System.err.println("another drag is already in progress");
            event.consume();
        });
        circle.setOnDragOver(event -> {
            if (isDragging() && circle != dragSource) {
                circle.setFill(Color.CYAN);
                event.acceptTransferModes(TransferMode.ANY);
            }
        });
        circle.setOnDragExited(event -> {
            if (isDragging() && circle != dragSource)
                circle.setFill(Color.BLUE);
        });
        circle.setOnDragDropped(event -> {
            System.out.println("circular drag dropped");
            if (!isDragging()) {
                // this should not occur
                System.err.println("ERROR: no drag source found");
                return;
            }
            lineBetween(dragSource, circle);
            dragSource.setFill(Color.BLUE);
            circle.setFill(Color.BLUE);
            dragSource = null;
            event.setDropCompleted(true);
            event.consume();
        });
        root.getChildren().add(circle);
    }

    private void lineBetween(Circle source, Circle dest) {
        Line edge = new Line(
            source.getCenterX(), source.getCenterY(),
            dest.getCenterX(), dest.getCenterY()
        );
        edge.setStroke(Color.AQUA);
        edge.setStrokeWidth(10);
        edge.setStrokeLineCap(StrokeLineCap.ROUND);
        root.getChildren().add(edge);
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
