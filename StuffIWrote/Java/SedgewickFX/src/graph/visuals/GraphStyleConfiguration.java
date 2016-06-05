package graph.visuals;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Ethan Petuchowski 5/31/16
 */
public class GraphStyleConfiguration {
    private Paint backgroundColor = Color.BLACK;
    private Paint nodeColor = Color.LIGHTGRAY;
    private Paint edgeColor = Color.LIGHTBLUE;
    private int nodeSize = 35;
    private double edgeWidth = 20;

    public Paint getBackgroundColor() {
        return backgroundColor;
    }
    public void setBackgroundColor(Paint backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    public Paint getNodeColor() {
        return nodeColor;
    }
    public void setNodeColor(Paint nodeColor) {
        this.nodeColor = nodeColor;
    }
    public Paint getEdgeColor() {
        return edgeColor;
    }
    public void setEdgeColor(Paint edgeColor) {
        this.edgeColor = edgeColor;
    }
    public int getNodeSize() {
        return nodeSize;
    }
    public void setNodeSize(int nodeSize) {
        this.nodeSize = nodeSize;
    }
    public double getEdgeWidth() {
        return edgeWidth;
    }
    public void setEdgeWidth(double edgeWidth) {
        this.edgeWidth = edgeWidth;
    }
}
