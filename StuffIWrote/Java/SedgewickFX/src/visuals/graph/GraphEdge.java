package visuals.graph;

import javafx.geometry.Point2D;

/**
 * Ethan Petuchowski 5/31/16
 */
public class GraphEdge {
    private GraphNode fromNode;
    private GraphNode toNode;
    private GraphEdge(GraphNode fromNode, GraphNode toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }
    public static GraphEdgeBuilder from(GraphNode coordinates) {
        return new GraphEdgeBuilder(coordinates);
    }
    @Override public String toString() { return fromNode+" -> "+toNode; }
    public GraphNode getFromNode() { return fromNode; }
    public GraphNode getToNode() { return toNode; }

    public Point2D getFromCoords() { return getFromNode().getCanvasCoordinates(); }
    public Point2D getToCoords() { return getToNode().getCanvasCoordinates(); }

    @SuppressWarnings("WeakerAccess")
    public static class GraphEdgeBuilder {
        GraphNode fromCoords;
        GraphEdgeBuilder(GraphNode coordinates) {
            this.fromCoords = coordinates;
        }
        public GraphEdge to(GraphNode toCoords) {
            return new GraphEdge(fromCoords, toCoords);
        }
    }
}
