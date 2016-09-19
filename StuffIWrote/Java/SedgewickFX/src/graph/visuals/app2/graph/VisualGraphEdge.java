package graph.visuals.app2.graph;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * 9/18/16 10:57 PM
 */
class VisualGraphEdge {
    private final Line line;
    private final VisualGraphNode from;
    private final VisualGraphNode to;

    public VisualGraphEdge(VisualGraphNode from, VisualGraphNode to) {
        this.line = new Line(from.xLoc(), from.yLoc(), to.xLoc(), to.yLoc());
        this.line.setFill(Color.LIGHTBLUE);
        this.from = from;
        this.to = to;
    }

    public Line getLine() {
        return line;
    }
}
