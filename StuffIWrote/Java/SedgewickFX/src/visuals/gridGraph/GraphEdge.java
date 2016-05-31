package visuals.gridGraph;

import javafx.geometry.Point2D;

/**
 * Ethan Petuchowski 5/31/16
 */
class GraphEdge {
    private GraphNode fromNode;
    private GraphNode toNode;
    private GraphEdge(GraphNode fromNode, GraphNode toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }
    static GraphEdgeBuilder from(GraphNode coordinates) {
        return new GraphEdgeBuilder(coordinates);
    }
    @Override public String toString() { return fromNode+" -> "+toNode; }
    GraphNode getFromNode() { return fromNode; }
    GraphNode getToNode() { return toNode; }

    Point2D getFromCoords() { return getFromNode().getCanvasCoordinates(); }
    Point2D getToCoords() { return getToNode().getCanvasCoordinates(); }

    @SuppressWarnings("WeakerAccess")
    static class GraphEdgeBuilder {
        GraphNode fromCoords;
        GraphEdgeBuilder(GraphNode coordinates) {
            this.fromCoords = coordinates;
        }
        GraphEdge to(GraphNode toCoords) {
            return new GraphEdge(fromCoords, toCoords);
        }
    }
}
