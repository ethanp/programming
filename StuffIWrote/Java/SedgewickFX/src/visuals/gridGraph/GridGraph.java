package visuals.gridGraph;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Ethan Petuchowski 5/30/16
 */
class GridGraph {

    GridGraph(GraphicsContext graphicsContext) {
        List<GraphNode> nodes = createNodeGrid();
        GridPainter gridPainter = new GridPainter(graphicsContext);
        gridPainter.drawGrid(nodes);
    }

    private List<GraphNode> createNodeGrid() {
        int nodeRows = 5;
        int nodeCols = 5;
        List<GraphNode> ret = new ArrayList<>();
        for (int row = 0; row < nodeRows; row++)
            for (int col = 0; col < nodeCols; col++)
                ret.add(GraphNode.at(row, col));
        return ret;
    }


}
