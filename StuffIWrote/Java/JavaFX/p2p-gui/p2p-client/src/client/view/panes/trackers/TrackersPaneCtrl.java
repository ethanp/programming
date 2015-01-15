package client.view.panes.trackers;

import client.util.TreeTableRoot;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;

import java.util.Arrays;
import java.util.List;

/**
 * Ethan Petuchowski 1/9/15
 *
 * It looks like this:
 * ===========================================================
 * ||   Tracker/FileName       ||  Size     || #Sd  || #Lch ||
 * -----------------------------------------------------------
 * || \/ 2 Trackers            ||           ||      ||      ||
 * ||   \/ 123.tra.ker.adr:523 || 2 files   ||      ||      ||
 * ||       file1              ||  size1 B  ||  23  ||  31  ||
 * ||       file2              ||  size2 B  ||   6  ||   2  ||
 * ||   \/ 213.tra.ker.adr:412 || 2 files   ||      ||      ||
 * ||       file3              ||  size1 B  ||  21  ||  11  ||
 * ||       file4              ||  size2 B  ||   7  ||   4  ||
 * ||    ...                   ||  ...      || ...  || ...  ||
 * ===========================================================
 */
public class TrackersPaneCtrl {
    @FXML private TreeTableView<Celery> treeTable;
    @FXML private TreeTableColumn<Celery,Celery> nameCol;
    @FXML private TreeTableColumn<Celery,Celery> sizeCol;
    @FXML private TreeTableColumn<Celery,Celery> seedersCol;
    @FXML private TreeTableColumn<Celery,Celery> leechersCol;

    private List<TreeTableColumn<Celery,Celery>> tableColumns;

    TrackerTreeItem treeTableRoot = new TrackerTreeItem(new Celery(new TreeTableRoot()));


    Callback<TreeTableColumn.CellDataFeatures<Celery, Celery>, ObservableValue<Celery>>
            cellValueFactory = c -> new ReadOnlyObjectWrapper<>(c.getValue().getValue());

    @FXML private void initialize() {
        treeTable.setRoot(treeTableRoot);
        treeTable.setShowRoot(true);
        tableColumns = Arrays.asList(nameCol, sizeCol, seedersCol, leechersCol);
        tableColumns.forEach(c -> c.setCellValueFactory(cellValueFactory));

        nameCol.setCellFactory(e -> new TrackersCell(TrackersCell.Cols.NAME));
        sizeCol.setCellFactory(e -> new TrackersCell(TrackersCell.Cols.SIZE));
        seedersCol.setCellFactory(e -> new TrackersCell(TrackersCell.Cols.NUM_SEEDERS));
        leechersCol.setCellFactory(e -> new TrackersCell(TrackersCell.Cols.NUM_LEECHERS));
    }
}
