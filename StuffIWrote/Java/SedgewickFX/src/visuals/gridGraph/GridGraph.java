package visuals.gridGraph;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Ethan Petuchowski 5/30/16
 */
class GridGraph {

    private final int numRows = 5;
    private final int numCols = 5;
    private GridPainter gridPainter;
    List<GraphNode> nodes = new ArrayList<>();
    List<GraphEdge> edges = new ArrayList<>();

    GridGraph(GraphicsContext graphicsContext) {
        nodes = createNodeGrid();
        gridPainter = new GridPainter(graphicsContext, numRows, numCols);
        gridPainter.drawGrid(nodes);
    }

    private List<GraphNode> createNodeGrid() {
        List<GraphNode> ret = new ArrayList<>();
        for (int row = 0; row < numRows; row++)
            for (int col = 0; col < numCols; col++)
                ret.add(GraphNode.at(row, col));
        return ret;
    }


    void addRandomEdges(int numEdges) {
        for (int edgeNum = 0; edgeNum < numEdges; edgeNum++) {
            GridCoordinates fromCoords = randomGridLoc();
            GridCoordinates toCoords = randomGridLoc();
            GraphEdge randomEdge = GraphEdge.from(fromCoords).to(toCoords);
            System.out.println("drawing edge: "+randomEdge);
            gridPainter.drawEdge(randomEdge);
            edges.add(randomEdge);
        }
    }
    private GridCoordinates randomGridLoc() {
        Random r = new Random();
        return new GridCoordinates(
            r.nextInt(numRows),
            r.nextInt(numCols));
    }

}
