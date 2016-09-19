package graph.visuals.app2.graph;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/** representation of a user-created graph node */
class VisualGraphNode {
    private final Circle circle;

    VisualGraphNode(Point2D location) {
        circle = new Circle(location.getX(), location.getY(), 25, Color.BLUE);
    }

    public void setInactive() {
        circle.setFill(Color.BLUE);
    }

    public void setActive() {
        circle.setFill(Color.RED);
    }

    public Circle getCircle() {
        return circle;
    }

    public double xLoc() {
        return circle.getCenterX();
    }

    public double yLoc() {
        return circle.getCenterY();
    }
}
