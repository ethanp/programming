package visuals.gridGraph;

/**
 * Ethan Petuchowski 5/30/16
 */
class GraphNode {
    private GridCoordinates coordinates;

    private GraphNode(GridCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    static GraphNode at(int x, int y) {
        return new GraphNode(new GridCoordinates(x, y));
    }

    GridCoordinates getCoordinates() {
        return coordinates;
    }
}
