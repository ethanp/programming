package base.view.panes.trackers;

import base.util.TreeTableRoot;
import javafx.fxml.FXML;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;

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
    @FXML private TreeTableColumn<Celery,Celery> treeNameCol;
    @FXML private TreeTableColumn<Celery,Celery> treeSizeCol;
    @FXML private TreeTableColumn<Celery,Celery> treeSeedersCol;
    @FXML private TreeTableColumn<Celery,Celery> treeLeechersCol;

    SwarmTreeItem treeTableRoot = new SwarmTreeItem(new Celery(new TreeTableRoot()));

    @FXML private void initialize() {
        initializeTrkrTreeTable();
    }

    public void initializeTrkrTreeTable() {
        treeTable.setRoot(treeTableRoot);

        // Defining how cell content is extracted from each SwarmTreeRenderable for each column
        treeNameCol.setCellValueFactory(

                // option 1: using a provided property value-getting class
                new TreeItemPropertyValueFactory<>("name"));

                // option 2: using a read-only 'property' created on-the-fly
//                p -> new ReadOnlyStringWrapper(p.getValue().getValue().getName()));

//        treeSizeCol.setCellValueFactory(
//                p -> new ReadOnlyStringWrapper(p.getValue().getValue().getSizeString()));

        treeSeedersCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("numSeeders"));
        treeLeechersCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("numLeechers"));

        treeTable.setShowRoot(true); // show the "'root' tree-item"
    }
}
