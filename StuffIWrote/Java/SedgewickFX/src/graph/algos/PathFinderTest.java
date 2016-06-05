package graph.algos;

import graph.core.BasicGraphNode;
import graph.core.Graph;
import graph.core.GraphEdge;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Ethan Petuchowski 6/4/16
 */
public class PathFinderTest {
    @org.junit.Test
    public void dfs() throws Exception {
        System.out.println("testing PathFinder.dfs()");
        List<BasicGraphNode> nodes = Arrays.asList(
            new BasicGraphNode(new Point2D(10, 10)),
            new BasicGraphNode(new Point2D(20, 20)),
            new BasicGraphNode(new Point2D(30, 30)),
            new BasicGraphNode(new Point2D(40, 40))
        );
        List<GraphEdge> edges = new ArrayList<>();
        for (int i = 0; i < nodes.size() - 1; i++) {
            edges.add(GraphEdge
                .from(nodes.get(i))
                .to(nodes.get(i + 1)));
        }
        Graph<BasicGraphNode> graph = new Graph<BasicGraphNode>(nodes, edges);
        List<GraphEdge> result = new PathFinder(nodes.get(0), nodes.get(nodes.size() - 1), graph).dfs();
        System.out.println("results:");
        result.stream().forEach(System.out::println);
    }

}
