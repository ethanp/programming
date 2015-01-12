package base.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.util.stream.Collectors;

/**
 * Ethan Petuchowski 1/12/15
 */
public class SwarmTreeItem extends TreeItem<SwarmTreeRenderable> {

    public SwarmTreeItem(SwarmTreeRenderable value) { super(value); }

    /** A leaf can not be expanded by the user, and as such will not show a disclosure
     *      node or respond to expansion requests.
     *  This is called often, so luckily it is quite inexpensive and needn't be cached. */
    @Override public boolean isLeaf() { return getValue().getSwarms()   == null
                                            && getValue().knownTrackers == null; }

    /**
     * TODO This is called frequently, so it recommended that the returned list be cached.
     * @return a list that contains the child TreeItems belonging to the TreeItem.
     */
    @Override public ObservableList<TreeItem<SwarmTreeRenderable>> getChildren() {

        /* if this is the root object, its children are the known Trackers*/
        if (getValue().knownTrackers != null) {
            return FXCollections.observableArrayList(
                    getValue()
                            .knownTrackers
                            .stream()
                            .map(t -> new TreeItem<>(new SwarmTreeRenderable(t)))
                            .collect(Collectors.toList()));
        }

        /* if this object represents a P2PFile, it has no children */
        if (getValue().swarms == null)
            return null;

        /* if it represents a Tracker, it has P2PFile children
         * which need be wrapped in SwarmTreeRenderables and rendered too */
        return FXCollections.observableArrayList(
                getValue().getSwarms().stream()
                      .map(s -> new TreeItem<>(new SwarmTreeRenderable(s)))
                      .collect(Collectors.toList()));
    }

}
