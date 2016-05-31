package visuals.gridGraph;

/**
 * Ethan Petuchowski 5/31/16
 */
class GraphEdge {
    @Override public String toString() {
        return "GraphEdge{"+fromCoords+", "+toCoords+'}';
    }
    private GridCoordinates fromCoords;
    private GridCoordinates toCoords;

    private GraphEdge(GridCoordinates fromCoords, GridCoordinates toCoords) {
        this.fromCoords = fromCoords;
        this.toCoords = toCoords;
    }
    static GraphEdgeBuilder from(GridCoordinates coordinates) {
        return new GraphEdgeBuilder(coordinates);
    }
    GridCoordinates getFromCoords() {
        return fromCoords;
    }
    GridCoordinates getToCoords() {
        return toCoords;
    }

    @SuppressWarnings("WeakerAccess")
    static class GraphEdgeBuilder {
        GridCoordinates fromCoords;
        GraphEdgeBuilder(GridCoordinates coordinates) {
            this.fromCoords = coordinates;
        }
        GraphEdge to(GridCoordinates toCoords) {
            return new GraphEdge(fromCoords, toCoords);
        }
    }
}
