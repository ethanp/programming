package client.view.panes.files;

import client.Main;
import client.util.ClientStateUtiil;
import client.util.ViewUtil;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import p2p.file.P2PFile;
import p2p.tracker.Tracker;

/**
 * Ethan Petuchowski 1/14/15
 */
public class LocalFileCell extends TableCell<P2PFile, P2PFile> {
    enum Col { NAME, SIZE, PERCENT}
    private Col c;
    private ContextMenu menu = new ContextMenu();

    public LocalFileCell(Col column) {
        super();
        c = column;
    }

    private String getTxt() {
        switch (c) {
            case NAME:    return getItem().getFilename();
            case SIZE:    return getItem().getFilesizeString();
            case PERCENT: return getItem().getCompletenessString();
        }
        throw new RuntimeException("unreachable");
    }

    /**
     * The updateItem method is the best method for developers to override
     * to customise the visuals of the cell. Developers should never call
     * this method in their code. However, the purpose of having the
     * updateItem method is when specifying custom cell factories, the
     * updateItem method can be overridden to allow for complete
     * customisation of the cell.
     * If it is empty, then it does not represent any domain data, but is a
     * cell being used to render an "empty" row.
     */
    @Override protected void updateItem(P2PFile item, boolean empty) {
        super.updateItem(item, empty);
        menu = new ContextMenu();
        if (empty || item == null) {
            setText(null); setGraphic(null);
        }
        else {
            setText(getTxt());
            ViewUtil.addOpt(menu, "Remove file from list", e->Main.localFiles.remove(getItem()));
            ViewUtil.addOpt(menu, "Add file to tracker", e->{});
        }
        ViewUtil.addOpt(menu, "Add new fake file", e-> ClientStateUtiil.addFakeLocalFile());
        ViewUtil.showOnRightClick(this, menu);
    }

    // TODO not finished
    private ContextMenu getSendToTrackerMenu() {
        ContextMenu contextMenu = new ContextMenu();
        for (Tracker tracker : Main.knownTrackers) {
            MenuItem menuItem = new MenuItem(tracker.getIpPortString());
            menuItem.setOnAction(e -> tracker.createSwarmFor(getItem()));
        }
        return contextMenu;
    }
}
