package visuals.gridGraph;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import visuals.graph.GraphEdge;
import visuals.graph.GraphNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Ethan Petuchowski 5/30/16
 */
class GridGraph {

    private final int numRows = 5;
    private final int numCols = 5;
    private final GridPainter gridPainter;
    private final Graph<GridGraphNode> graph = new Graph<>();

    GridGraph(GraphicsContext graphicsContext) {
        graph.addNodes(createNodeGrid());
        gridPainter = new GridPainter(graphicsContext, numRows, numCols);
        gridPainter.redrawNodeGrid(graph.getNodes());
    }

    private List<GridGraphNode> createNodeGrid() {
        List<GridGraphNode> ret = new ArrayList<>();
        for (int row = 0; row < numRows; row++)
            for (int col = 0; col < numCols; col++)
                ret.add(new GridGraphNode(row, col));
        return ret;
    }

    void addRandomEdges(int numEdges) {
        graph.addRandomEdges(numEdges);
        drawEdges();
    }

    private void drawEdges() {
        graph.getEdges().forEach(gridPainter::drawEdge);
    }

    List<GraphEdge> findPath(GraphEdge between) {
        return GraphAlgos.findPath(between.getFromNode(), between.getToNode(), this.graph);
    }

    private static class GraphAlgos {
        static List<GraphEdge> findPath(GraphNode fromNode, GraphNode toNode, Graph graph) {
            List<GraphEdge> edges = new ArrayList<>();
            edges.add(GraphEdge.from(fromNode).to(toNode));
            return edges;
        }
    }

    private static class Graph<NodeT extends GraphNode> {
        private final List<NodeT> nodes = new ArrayList<>();
        private final List<GraphEdge> edges = new ArrayList<>();
        Graph() {}
        Graph(List<NodeT> nodes, List<GraphEdge> edges) {
            this.nodes.addAll(nodes);
            this.edges.addAll(edges);
        }
        GraphNode getRandomNode() {
            return nodes.get(new Random().nextInt(nodes.size()));
        }
        void addNodes(List<NodeT> nodes) {
            this.nodes.addAll(nodes);
        }
        void addRandomEdges(int numEdges) {
            for (int edgeNum = 0; edgeNum < numEdges; edgeNum++) {
                GraphEdge randomEdge = GraphEdge.from(getRandomNode()).to(getRandomNode());
                System.out.println("adding edge: "+randomEdge);
                edges.add(randomEdge);
            }
        }
        List<GraphEdge> getEdges() {
            return edges;
        }
        List<NodeT> getNodes() {
            return nodes;
        }
    }

    class GridGraphNode implements GraphNode {
        private GridCoordinates coordinates;

        private GridGraphNode(int row, int col) {
            this.coordinates = new GridCoordinates(row, col);
        }
        @Override public String toString() {
            return "Node("+coordinates+')';
        }
        GridCoordinates getCoordinates() {
            return coordinates;
        }
        @Override public Point2D getCanvasCoordinates() {
            return gridPainter.canvasLocForGridCoords(coordinates);
        }
    }

}
