package visuals.gridGraph;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Ethan Petuchowski 5/30/16
 */
class GridGraph {

    private final int numRows = 5;
    private final int numCols = 5;

    GridGraph(GraphicsContext graphicsContext) {
        List<GraphNode> nodes = createNodeGrid();
        GridPainter gridPainter = new GridPainter(graphicsContext, numRows, numCols);
        gridPainter.drawGrid(nodes);
    }

    private List<GraphNode> createNodeGrid() {
        List<GraphNode> ret = new ArrayList<>();
        for (int row = 0; row < numRows; row++)
            for (int col = 0; col < numCols; col++)
                ret.add(GraphNode.at(row, col));
        return ret;
    }


}
