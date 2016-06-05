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
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
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
            click.consume();
        });
        scene.setOnDragDropped(dragDrop -> {
            System.out.println("INFO: cancelling drag");
            dragSource = null;
            dragDrop.setDropCompleted(true);
            dragDrop.consume();
        });
        scene.setOnDragDetected(event -> {
            System.out.println("scene drag detected");
            ClipboardContent content = new ClipboardContent();
            content.putString("okey dokey");
            root.startDragAndDrop(TransferMode.ANY).setContent(content);
            event.consume();
        });
        scene.setOnDragDone(event -> {
            System.out.println("drag done in the open");
            event.consume();
        });
        scene.setOnDragDropped(event -> {
            System.out.println("drag dropped in the open");
            event.setDropCompleted(true);
            event.consume();
        });
    }

    private boolean isDragging() {
        return dragSource != null;
    }

    void addNodeAt(Point2D point) {
        Circle circle = new Circle(point.getX(), point.getY(), 25);
        circle.setFill(Color.BLUE);
        circle.setOnDragDetected(event -> {
            System.out.println("circle drag detected");
            if (!isDragging()) {
                ClipboardContent content = new ClipboardContent();
                content.putString("circle drag");
                circle.startDragAndDrop(TransferMode.ANY).setContent(content);
                circle.setFill(Color.GREEN);
                dragSource = circle;
            }
            else System.err.println("another drag is already in progress");
            event.consume();
        });
        circle.setOnDragDone(event -> {
            dragSource = null;
            System.out.println("circle drag done");
            circle.setFill(Color.BLUE);
            event.consume();
        });
        circle.setOnDragOver(event -> {
            if (circle != dragSource)
                circle.setFill(Color.CYAN);
            event.acceptTransferModes(TransferMode.ANY);
            event.consume();
        });
        circle.setOnDragExited(event -> {
            if (circle != dragSource)
                circle.setFill(Color.BLUE);
            event.consume();
        });
        circle.setOnDragDropped(event -> {
            System.out.println("circular drag dropped");
            Object c = event.getDragboard().getContent(DataFormat.PLAIN_TEXT);
            if (!isDragging()) {
                System.err.println("ERROR: no drag source found");
                return;
            }
            circle.setFill(Color.RED);
            Line edge = new Line(
                dragSource.getCenterX(), dragSource.getCenterY(),
                circle.getCenterX(), circle.getCenterY()
            );
            edge.setStroke(Color.AQUA);
            edge.setStrokeWidth(20);
            root.getChildren().add(edge);
            dragSource = null;
            event.setDropCompleted(true);
            event.consume();
        });
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
