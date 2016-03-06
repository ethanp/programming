package visuals;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import util.Pair;

/**
 * Ethan Petuchowski 3/5/16
 */
public class NonNegativeLineGraph {
    final GraphicsContext gc;
    final double height;
    final double width;
    final double graphProportion = .8;
    private ObservableList<Pair<Double>> values = null;

    public NonNegativeLineGraph(GraphicsContext gc) {
        this.gc = gc;
        this.height = gc.getCanvas().getHeight();
        this.width = gc.getCanvas().getWidth();
    }

    public void drawBasedOn(ObservableList<Pair<Double>> values) {
        this.values = values;
        this.values.addListener((ListChangeListener<? super Pair<Double>>) c -> redraw());
        redraw();
    }

    public double maxValue() {
        return values
            .stream()
            .map(Pair::getB)
            .max(Double::compare)
            .get();
    }

    private void redraw() {
        double xScale = width*graphProportion/values.size();
        double yScale = height*graphProportion/maxValue();
        Main.clearCanvas();
        drawAxisLines();
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

    private Pair<Double> getLowerLeftCorner() {
        return new Pair<>(
            (width-width*graphProportion)/2,
            height-(height-height*graphProportion)/2
        );
    }

    private Pair<Double> getLowerRightCorner() {
        return new Pair<>(
            width-(width-width*graphProportion)/2,
            height-(height-height*graphProportion)/2
        );
    }

    private Pair<Double> getTopLeftCorner() {
        return new Pair<>(
            (width-width*graphProportion)/2,
            (height-height*graphProportion)/2
        );
    }
}
