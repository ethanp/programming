package graph.core;

import javafx.geometry.Point2D;

/**
 * Ethan Petuchowski 6/4/16
 */
public class BasicGraphNode extends GraphNode {

    private Point2D canvasCoordinates;

    public BasicGraphNode(Point2D canvasCoordinates) {
        this.canvasCoordinates = canvasCoordinates;
    }

    @Override protected Point2D getCanvasCoordinates() {
        return canvasCoordinates;
    }
}
