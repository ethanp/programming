package visuals.gridGraph;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;

/**
 * Ethan Petuchowski 5/30/16
 */
class GridPainter {
    private GridStyleConfiguration styleConfig = new GridStyleConfiguration();

    private GraphicsContext graphicsContext;

    GridPainter(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    private double canvasWidth() {
        return graphicsContext.getCanvas().getWidth();
    }

    private double canvasHeight() {
        return graphicsContext.getCanvas().getHeight();
    }

    void drawGrid(List<GraphNode> nodes) {
        setBackground();
        nodes.forEach(this::renderNode);
    }

    private void renderNode(GraphNode node) {
        graphicsContext.setFill(styleConfig.nodeColor);
        drawCircle(styleConfig.nodeSize, node.getCoordinates());
    }

    private void setBackground() {
        graphicsContext.setFill(styleConfig.backgroundColor);
        graphicsContext.fillRect(0, 0, canvasHeight(), canvasWidth());
    }

    private void drawCircle(int size, GridCoordinates coordinates) {
        Point2D point2D = canvasLocForGridCoords(coordinates);
        graphicsContext.fillOval(
            point2D.getX(),
            point2D.getY(),
            (double) size,
            (double) size
        );
    }

    private Point2D canvasLocForGridCoords(GridCoordinates coordinates) {
        double columnWidth = -1;
        double leftColumnLoc = -1;
        double columnLoc = leftColumnLoc+columnWidth*coordinates.getColumnNumber();
        double rowHeight = -1;
        double topRowLoc = -1;
        double rowLoc = topRowLoc+rowHeight*coordinates.getRowNumber();
        return new Point2D(columnLoc, rowLoc);
    }

    private static class GridStyleConfiguration {
        Paint backgroundColor = Color.BEIGE;
        Paint nodeColor = Color.AQUA;
        int nodeSize = 15;
    }
}
