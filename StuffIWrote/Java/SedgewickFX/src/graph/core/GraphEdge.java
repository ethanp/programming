package graph.core;

import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

/**
 * Ethan Petuchowski 5/31/16
 *
 * Because `equals(other)` is "by reference", one can create multiple equal edges.
 * This means that if you create a copy, you cannot check for equality anymore.
 * To retain both the ability to create equal edges and create copies, one could
 * create a UUID for each edge.
 */
public class GraphEdge {
    private GraphNode fromNode;
    private GraphNode toNode;
    private double weight;

    private GraphEdge(GraphNode fromNode, @NotNull GraphNode toNode, double weight) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.weight = weight;
    }

    public static UnweightedGraphEdgeBuilder from(GraphNode coordinates) {
        return new UnweightedGraphEdgeBuilder(coordinates);
    }

    public static WeightedGraphEdgeBuilder withWeight(double weight) {
        return new WeightedGraphEdgeBuilder(weight);
    }

    @Override public String toString() {
        return fromNode + " -> " + toNode;
    }

    public GraphNode getFromNode() {
        return fromNode;
    }

    public GraphNode getToNode() {
        return toNode;
    }

    public double getWeight() {
        return weight;
    }

    public Point2D getFromCoords() {
        return getFromNode().getCanvasCoordinates();
    }

    public Point2D getToCoords() {
        return getToNode().getCanvasCoordinates();
    }

    // this warning is incorrect!
    @SuppressWarnings("WeakerAccess")
    public static class UnweightedGraphEdgeBuilder {
        GraphNode fromNode;

        UnweightedGraphEdgeBuilder(GraphNode node) {
            this.fromNode = node;
        }

        public GraphEdge to(GraphNode toNode) {
            return new GraphEdge(this.fromNode, toNode, 1);
        }
    }

    private static class WeightedGraphEdgeBuilder {
        private GraphNode fromNode;
        private GraphNode toNode;
        private double weight;

        WeightedGraphEdgeBuilder(double weight) {
            this.weight = weight;
        }

        WeightedGraphEdgeBuilder from(GraphNode node) {
            this.fromNode = node;
            return this;
        }

        GraphEdge to(GraphNode toNode) {
            return new GraphEdge(this.fromNode, toNode, this.weight);
        }
    }
}
