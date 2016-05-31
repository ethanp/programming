package visuals.graph;

import javafx.geometry.Point2D;

/**
 * Ethan Petuchowski 5/30/16
 */
public abstract class GraphNode {
    protected abstract Point2D getCanvasCoordinates();
    @Override public int hashCode() {
        return getCanvasCoordinates().hashCode();
    }
    @Override public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphNode g = (GraphNode) o;
        return getCanvasCoordinates().equals(g.getCanvasCoordinates());
    }
}
