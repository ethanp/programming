package graph.visuals.linkedlist;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 8/27/16 6:54 PM
 */
public class Interactivity {
    private final Group root;
    Deque<ListNode> linkedList = new LinkedList<>();

    Interactivity(Scene scene) {
        this.root = (Group) scene.getRoot();
    }

    public void addFront(String contents, MouseEvent click) {
        linkedList.addFirst(new ListNode(contents));
        renderList();
    }

    private void renderList() {
        /* TODO find bounds */
        double minX = 0;
        double maxX = 0;
        double minY = 0;
        double maxY = 0;
        double width = 0;
        double height = 0;

        /* TODO find space per node-link pair */
        double widthPerNodeLinkPair = 0;
        double widthPerLink = 0;
        double widthPerNode = 0;

        /* draw each pair in its slot */
        Iterator<ListNode> it = linkedList.iterator();
        for (int i = 0; i < linkedList.size(); i++) {
            // TODO see http://stackoverflow.com/questions/17437411/
            // how-to-put-a-text-into-a-circle-object-to-display-it-from-circles-center
            Circle circle = new Circle(widthPerNodeLinkPair*i, minY, widthPerNode);
            Text circleLabel = new Text(it.next().contents);
            circleLabel.setBoundsType(TextBoundsType.VISUAL);
            StackPane stack = new StackPane();
            stack.getChildren().addAll(circle, circleLabel);
            root.getChildren().add(stack);
        }
    }

    static class ListNode {
        String contents;

        public ListNode(String contents) {
            this.contents = contents;
        }
    }
}
