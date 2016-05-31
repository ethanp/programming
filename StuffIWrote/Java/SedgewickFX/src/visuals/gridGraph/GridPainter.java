package visuals.gridGraph;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.jetbrains.annotations.Contract;

import java.util.List;

/**
 * Ethan Petuchowski 5/30/16
 */
class GridPainter {
    private GridStyleConfiguration styleConfig = new GridStyleConfiguration();

    private GraphicsContext graphicsContext;

    private int numRows;
    private int numCols;

    GridPainter(GraphicsContext graphicsContext, int numCols, int numRows) {
        this.graphicsContext = graphicsContext;
        this.numCols = numCols;
        this.numRows = numRows;
    }

    private double canvasWidth() { return graphicsContext.getCanvas().getWidth(); }

    private double canvasHeight() { return graphicsContext.getCanvas().getHeight(); }

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
        double columnLoc = leftColumnLoc()+columnWidth()*coordinates.getColumnNumber();
        double rowLoc = topRowLoc()+rowHeight()*coordinates.getRowNumber();
        return new Point2D(columnLoc, rowLoc);
    }

    // I think the @Contract is for intellij's static analysis features
    @Contract(pure = true) private double canvasScale() { return .8; }
    @Contract(pure = true) private double canvasMargin() {return (1-canvasScale())/2;}
    @Contract(pure = true) private double gridHeight() {return canvasHeight()*canvasScale();}
    @Contract(pure = true) private double gridWidth() {return canvasWidth()*canvasScale();}
    @Contract(pure = true) private double rowHeight() { return gridHeight()/numRows; }
    @Contract(pure = true) private double columnWidth() {return gridWidth()/numCols;}
    @Contract(pure = true) private double topRowLoc() { return canvasHeight()*canvasMargin(); }
    @Contract(pure = true) private double leftColumnLoc() {return canvasWidth()*canvasMargin();}

    void drawEdge(GraphEdge edge) {
        Point2D g1 = canvasLocForGridCoords(edge.getFromCoords());
        Point2D g2 = canvasLocForGridCoords(edge.getToCoords());
        graphicsContext.setStroke(styleConfig.edgeColor);
        graphicsContext.setLineWidth(styleConfig.edgeWidth);
        graphicsContext.moveTo(g1.getX(), g1.getY());
        graphicsContext.lineTo(g2.getX(), g2.getY());
        graphicsContext.stroke();
    }

    private static class GridStyleConfiguration {
        Paint backgroundColor = Color.BLACK;
        Paint nodeColor = Color.LIGHTGRAY;
        Paint edgeColor = Color.LIGHTBLUE;
        int nodeSize = 35;
        double edgeWidth = 20;
    }
}
