package base.view;

import base.p2p.tracker.Swarm;
import base.p2p.tracker.Tracker;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ethan Petuchowski 1/12/15
 */
public class SwarmTreeItem extends TreeItem<SwarmTreeRenderable> {

    public SwarmTreeItem(SwarmTreeRenderable value) {
        super(value);
        if (getValue().isRoot()) {
            getValue().getKnownTrackers().addListener(rootListener);
            for (Tracker tracker : getValue().getKnownTrackers())
                children.add(new SwarmTreeItem(new SwarmTreeRenderable(tracker)));
        }
        else if (getValue().isTracker()) {
            getValue().getSwarms().addListener(trackerListener);
            for (Swarm swarm : getValue().getSwarms())
                children.add(new SwarmTreeItem(new SwarmTreeRenderable(swarm)));
        }
    }

    /** A leaf can not be expanded by the user, and as such will not show a disclosure
     *      node or respond to expansion requests.
     *  This is called often, so luckily it is quite inexpensive and needn't be cached. */
    @Override public boolean isLeaf() { return getValue().isFile(); }

    public SwarmTreeItem addTracker(Tracker tracker) {
        if (!getValue().isRoot()) return null;
        getValue().getKnownTrackers().add(tracker);
        return this;
    }

    ObservableList<TreeItem<SwarmTreeRenderable>> children = FXCollections.observableArrayList();

    private void updateRootChildren(List<? extends Tracker> removed,
                                    List<? extends Tracker> added) {
        for (Tracker tracker : removed) {
            TreeItem<SwarmTreeRenderable> toRemove = null;
            for (TreeItem<SwarmTreeRenderable> child : children) {
                if (child.getValue().getSwarms().equals(tracker.getSwarms())) {
                    toRemove = child;
                    break;
                }
            }
            if (toRemove == null) throw new RuntimeException("child wasn't found");
            children.remove(toRemove);
        }
        for (Tracker trkr : added) children.add(new SwarmTreeItem(new SwarmTreeRenderable(trkr)));
    }

    private void updateTrackerChildren(List<? extends Swarm> removed,
                                       List<? extends Swarm> added) {
        for (Swarm swarm: removed) {
            TreeItem<SwarmTreeRenderable> toRemove = null;
            for (TreeItem<SwarmTreeRenderable> child : children) {
                if (child.getValue().getName().equals(swarm.getP2pFile().getFilename())) {
                    toRemove = child;
                    break;
                }
            }
            if (toRemove == null) throw new RuntimeException("child wasn't found");
            children.remove(toRemove);
        }
        for (Swarm swarm : added) children.add(new SwarmTreeItem(new SwarmTreeRenderable(swarm)));
    }

    private final ListChangeListener<Tracker> rootListener = c -> {
        Logger.getGlobal().log(Level.INFO, "rootListener observed event");
        while (c.next()) {
            if (c.wasAdded() || c.wasRemoved())
                updateRootChildren(c.getRemoved(), c.getAddedSubList());
            else if (c.wasUpdated()) throw new RuntimeException("we don't handle updates");
            else if (c.wasPermutated()) throw new RuntimeException("we don't handle permutations");
        }
    };

    private final ListChangeListener<Swarm> trackerListener = c -> {
        Logger.getGlobal().log(Level.INFO, "trackerListener observed event");
        while (c.next()) {
            if (c.wasAdded() || c.wasRemoved())
                updateTrackerChildren(c.getRemoved(), c.getAddedSubList());
            else if (c.wasUpdated()) throw new RuntimeException("we don't handle updates");
            else if (c.wasPermutated()) throw new RuntimeException("we don't handle permutations");
        }
    };

    /**
     * This is called frequently, so the returned list is cached.
     * @return a list that contains the child TreeItems belonging to the TreeItem.
     */
    @Override public ObservableList<TreeItem<SwarmTreeRenderable>> getChildren() {
        Logger.getGlobal().log(Level.INFO,
                               "Getting the "+children.size()+" children of: "+getValue().getName());
        return children;
    }
}
