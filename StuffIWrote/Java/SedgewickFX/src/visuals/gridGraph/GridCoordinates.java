package visuals.gridGraph;

/**
 * Ethan Petuchowski 5/30/16
 */
class GridCoordinates {
    private int rowNumber;
    private int columnNumber;

    GridCoordinates(int rowNumber, int columnNumber) {
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
    }

    int getRowNumber() {
        return rowNumber;
    }

    int getColumnNumber() {
        return columnNumber;
    }

    @Override public String toString() {
        return "GridCoordinates{"+rowNumber+", "+columnNumber+'}';
    }
}
