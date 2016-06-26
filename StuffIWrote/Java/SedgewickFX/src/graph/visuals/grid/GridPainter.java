package graph.visuals.grid;

import graph.core.GraphEdge;
import graph.visuals.GraphStyleConfiguration;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import org.jetbrains.annotations.Contract;

import java.util.List;

/**
 * Ethan Petuchowski 5/30/16
 */
class GridPainter {
    private final GraphStyleConfiguration style = new GraphStyleConfiguration();
    private final GraphicsContext graphicsContext;
    private final int numRows;
    private final int numCols;

    GridPainter(GraphicsContext graphicsContext, int numCols, int numRows) {
        this.graphicsContext = graphicsContext;
        this.numCols = numCols;
        this.numRows = numRows;
    }

    void redrawNodeGrid(List<GridGraph.GridGraphNode> nodes) {
        clearCanvas();
        nodes.forEach(this::renderNode);
    }

    private void renderNode(GridGraph.GridGraphNode node) {
        graphicsContext.setFill(style.getNodeColor());
        drawCircle(style.getNodeSize(), node.getCoordinates());
    }

    private void clearCanvas() {
        graphicsContext.setFill(style.getBackgroundColor());
        graphicsContext.fillRect(0, 0, canvasHeight(), canvasWidth());
    }

    private void drawCircle(int size, GridCoordinates coordinates) {
        Point2D point2D = canvasLocForGridCoords(coordinates);
        graphicsContext.fillOval(point2D.getX(), point2D.getY(), (double) size, (double) size);
    }

    Point2D canvasLocForGridCoords(GridCoordinates coordinates) {
        double columnLoc = leftColumnLoc()+columnWidth()*coordinates.getColumnNumber();
        double rowLoc = topRowLoc()+rowHeight()*coordinates.getRowNumber();
        return new Point2D(columnLoc, rowLoc);
    }

    // I think the @Contract is for intellij's static analysis features

    @Contract(pure = true) private double canvasScale() {
        return .8;
    }

    @Contract(pure = true) private double canvasMargin() {
        return (1-canvasScale())/2;
    }

    @Contract(pure = true) private double canvasWidth() {
        return graphicsContext.getCanvas().getWidth();
    }

    @Contract(pure = true) private double canvasHeight() {
        return graphicsContext.getCanvas().getHeight();
    }

    @Contract(pure = true) private double gridHeight() {
        return canvasHeight()*canvasScale();
    }

    @Contract(pure = true) private double gridWidth() {
        return canvasWidth()*canvasScale();
    }

    @Contract(pure = true) private double rowHeight() {
        return gridHeight()/numRows;
    }

    @Contract(pure = true) private double columnWidth() {
        return gridWidth()/numCols;
    }

    @Contract(pure = true) private double topRowLoc() {
        return canvasHeight()*canvasMargin();
    }

    @Contract(pure = true) private double leftColumnLoc() {
        return canvasWidth()*canvasMargin();
    }

    // XXX: this should be in a generic graph-painter class
    void drawEdge(GraphEdge edge) {
        Point2D g1 = edge.getFromCoords();
        Point2D g2 = edge.getToCoords();
        graphicsContext.setStroke(style.getEdgeColor());
        graphicsContext.setLineWidth(style.getEdgeWidth());
        graphicsContext.moveTo(g1.getX(), g1.getY());
        graphicsContext.lineTo(g2.getX(), g2.getY());
        graphicsContext.stroke();
    }

}
