package base.view.panes.files;

import base.Main;
import base.p2p.file.P2PFile;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;

/**
 * Ethan Petuchowski 1/14/15
 */
public class LocalFileCell extends TableCell<P2PFile, P2PFile> {

    enum Col { NAME, SIZE, PERCENT}
    private Col c;
    private ContextMenu contextMenu = new ContextMenu();

    public LocalFileCell(Col column) {
        super();
        c = column;
        MenuItem removeFile = new MenuItem("Remove file from list");
        contextMenu.getItems().addAll(removeFile);
        removeFile.setOnAction(event -> Main.localFiles.remove(getItem()));
        Main.showOnRightClick(this, contextMenu);
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
        if (empty || item == null) { setText(null); setGraphic(null); return; }
        setText(getTxt());
    }
}
