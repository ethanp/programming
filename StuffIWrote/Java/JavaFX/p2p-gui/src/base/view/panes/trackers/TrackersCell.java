package base.view.panes.trackers;

import base.Main;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeTableCell;

/**
 * Ethan Petuchowski 1/14/15
 */
public class TrackersCell extends TreeTableCell<Celery, Celery> {
    static enum Cols { NAME, SIZE, NUM_SEEDERS, NUM_LEECHERS }

    private Cols col;
    private ContextMenu contextMenu = new ContextMenu();

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

    private void addRootContextMenu() {
        MenuItem addTracker = new MenuItem("Add tracker");
        MenuItem removeTracker = new MenuItem("Remove tracker");
        contextMenu.getItems().addAll(addTracker, removeTracker);
        addTracker.setOnAction(event -> {});
        removeTracker.setOnAction(event -> {});
    }

    private void addSwarmContextMenu() {
        MenuItem downloadFile = new MenuItem("Download file");
        contextMenu.getItems().addAll(downloadFile);
        downloadFile.setOnAction(event -> {});
    }

    @Override protected void updateItem(Celery item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) { setText(null); setGraphic(null); return; }
        setText(myTxt());
        contextMenu = new ContextMenu();
        if (getItem().isRoot()) addRootContextMenu();
        else if (getItem().isSwarm()) addSwarmContextMenu();
        Main.showOnRightClick(this, contextMenu);
    }
}
