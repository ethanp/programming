package visuals;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import util.Pair;

import java.util.ListIterator;

/**
 * Ethan Petuchowski 3/5/16
 */
public class NonNegativeBarGraph {
    final GraphicsContext gc;
    final double totalHeight;
    final double totalWidth;
    final double graphHeight;
    final double graphWidth;
    final double graphProportion = .8;
    private ObservableList<Double> values = null;
    private double xScale = 0;
    private double yScale = 0;

    public NonNegativeBarGraph(GraphicsContext gc) {
        this.gc = gc;
        this.totalHeight = gc.getCanvas().getHeight();
        this.totalWidth = gc.getCanvas().getWidth();
        this.graphHeight = totalHeight*graphProportion;
        this.graphWidth = totalWidth*graphProportion;
    }

    public void drawBasedOn(ObservableList<Double> values) {
        this.values = values;
        this.values.addListener((ListChangeListener<? super Double>) c -> redraw());
        redraw();
    }

    private void redraw() {
        Main.clearCanvas();
        drawAxisLines();
        setXScale();
        setVerticalScale();
        drawBars();
    }
    private void drawBars() {
        ListIterator<Double> it = values.listIterator();
        gc.setLineWidth(30);
        gc.setLineCap(StrokeLineCap.BUTT);
        int idx = 0;
        while (it.hasNext()) {
            double barX = ++idx*xScale+getLeftSide()-gc.getLineWidth()/2;
            double val = it.next();
            double barY = getBottomSide()-val*yScale;
            gc.setStroke(Color.DARKTURQUOISE);
            gc.strokeLine(barX, getBottomSide(), barX, barY);
        }
    }

    private void drawAxisLines() {
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        Pair<Double> lowerLeftCorner = getLowerLeftCorner();
        Pair<Double> lowerRightCorner = getLowerRightCorner();
        Pair<Double> topLeftCorner = getTopLeftCorner();
        gc.strokeLine(lowerLeftCorner.a, lowerLeftCorner.b, lowerRightCorner.a, lowerRightCorner.b);
        gc.strokeLine(lowerLeftCorner.a, lowerLeftCorner.b, topLeftCorner.a, topLeftCorner.b);
    }

    public double maxValue() { return values.stream().max(Double::compare).get(); }

    private void setXScale() { xScale = graphWidth/values.size(); }

    private void setVerticalScale() { yScale = graphHeight/maxValue(); }

    private Double getLeftSide() { return (totalWidth-graphWidth)/2; }

    private Double getRightSide() { return (totalWidth+graphWidth)/2; }

    private Double getTopSide() { return (totalHeight-graphHeight)/2; }

    private Double getBottomSide() { return (totalHeight+graphHeight)/2; }

    private Pair<Double> getLowerLeftCorner() { return new Pair<>(getLeftSide(), getBottomSide()); }

    private Pair<Double> getLowerRightCorner() { return new Pair<>(getRightSide(), getBottomSide()); }

    private Pair<Double> getTopLeftCorner() { return new Pair<>(getLeftSide(), getTopSide()); }
}
