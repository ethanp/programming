package graph.visuals.app2.graph;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

/**
 * 9/18/16 10:57 PM
 */
class VisualGraphEdge {
    private final Line line;
    private final VisualGraphNode from;
    private final VisualGraphNode to;
    // probably best to use ints in practice
    private double weight;

    public VisualGraphEdge(VisualGraphNode from, VisualGraphNode to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.line = createLine();
    }

    public VisualGraphEdge(VisualGraphNode from, VisualGraphNode to) {
        this(from, to, 1);
    }

    private Line createLine() {
        Line line = new Line(from.xLoc(), from.yLoc(), to.xLoc(), to.yLoc());
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        line.setStroke(Color.LIGHTBLUE);
        line.setStrokeWidth(10);
        return line;
    }

    public Line getLine() {
        return line;
    }

    public VisualGraphNode getTo() {
        return to;
    }
}
