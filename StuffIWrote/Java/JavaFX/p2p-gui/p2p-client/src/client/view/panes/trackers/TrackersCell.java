package client.view.panes.trackers;

import client.Main;
import client.util.ClientStateUtiil;
import client.util.ViewUtil;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeTableCell;

/**
 * Ethan Petuchowski 1/14/15
 */
public class TrackersCell extends TreeTableCell<Celery, Celery> {
    static enum Cols { NAME, SIZE, NUM_SEEDERS, NUM_LEECHERS }

    private Cols col;
    private ContextMenu menu = new ContextMenu();

    private String myTxt() {
        switch (col) {
            case NAME:          return getItem().getName();
            case SIZE:          return getItem().getSize();
            case NUM_SEEDERS:   return getItem().getNumSeeders();
            case NUM_LEECHERS:  return getItem().getNumLeechers();
        }
        throw new RuntimeException("unreachable");
    }

    public TrackersCell(Cols c) {
        super();
        col = c;
    }

    @Override protected void updateItem(Celery item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            return;
        }
        setText(myTxt());
        menu = new ContextMenu();

        if (getItem().isRoot()) {
            ViewUtil.addOpt(menu, "Add fake tracker", e -> ClientStateUtiil.addFakeTracker());
        }
        if (getItem().isTracker()) {
            ViewUtil.addOpt(menu, "Add fake tracker", e->ClientStateUtiil.addFakeTracker());
            ViewUtil.addOpt(menu, "Remove tracker", e ->
                    Main.knownTrackers.remove(getItem().getTracker()));
        }
        if (getItem().isSwarm()) {
            ViewUtil.addOpt(menu, "Download file", e->{});
        }

        ViewUtil.showOnRightClick(this, menu);
    }
}
