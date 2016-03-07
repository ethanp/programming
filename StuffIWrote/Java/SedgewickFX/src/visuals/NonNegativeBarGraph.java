package visuals;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
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
    Color AXIS_COLOR = Color.RED;
    Color LABEL_COLOR = Color.WHITE;
    private ObservableList<Double> values = null;
    private double xScale = 0;
    private double yScale = 0;
    private boolean maxMayHaveChanged = true;
    private double storedMax;

    public NonNegativeBarGraph(GraphicsContext gc) {
        this.gc = gc;
        gc.setLineCap(StrokeLineCap.BUTT/*?*/);
        this.totalHeight = gc.getCanvas().getHeight();
        this.totalWidth = gc.getCanvas().getWidth();
        this.graphHeight = totalHeight*graphProportion;
        this.graphWidth = totalWidth*graphProportion;
    }

    public void drawBasedOn(ObservableList<Double> values) {
        this.values = values;
        this.values.addListener(
            (ListChangeListener<? super Double>) c -> {
                redraw();
                maxMayHaveChanged = true;
            }
        );
        redraw();
    }

    private void redraw() {
        Main.clearCanvas();
        drawAxisLines();
        setXScale();
        setVerticalScale();
        drawBars();
        drawYTicksAndLabels();
    }

    // SOMEDAY support logarithmic scale too
    private void drawYTicksAndLabels() {
        gc.setFont(Font.font(8));

        // choose number of ticks (take up ≤ 1/3 of the vertical space)
        int numTicks = (int) (graphHeight/gc.getFont().getSize()/3);

        // choose increments (based on number of ticks)
        double valueIncrement = maxValue()/numTicks;
        double heightIncrement = graphHeight/numTicks;

        double tickWidth = graphWidth/100;
        double leftEnd = getLeftSide()-tickWidth;
        double rightEnd = getLeftSide()+tickWidth;

        for (int tickIdx = 1; tickIdx <= numTicks; tickIdx++) {
            double tickHeight = getBottomSide()-tickIdx*heightIncrement;
            // draw tick
            gc.setStroke(AXIS_COLOR);
            gc.setLineWidth(1);
            gc.strokeLine(leftEnd, tickHeight, rightEnd, tickHeight);
            // draw label
            gc.setFill(LABEL_COLOR);
            String tickValue = String.format("%1.2f", valueIncrement*tickIdx);
            gc.fillText(tickValue, leftEnd-20, tickHeight, 20);
        }
    }

    private void drawBars() {
        ListIterator<Double> it = values.listIterator();
        double lineWidth = Math.min(25, graphWidth/values.size()*(2.0/3));
        gc.setLineWidth(lineWidth);
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
        gc.setStroke(AXIS_COLOR);
        gc.setLineWidth(3);
        drawLine(getLowerLeftCorner(), getLowerRightCorner());
        drawLine(getLowerLeftCorner(), getTopLeftCorner());
    }

    public double maxValue() {
        if (maxMayHaveChanged) {
            maxMayHaveChanged = false;
            storedMax = values.stream().max(Double::compare).get();
        }
        return storedMax;
    }

    private void drawLine(Pair<Double> a, Pair<Double> b) { gc.strokeLine(a.a, a.b, b.a, b.b); }

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
