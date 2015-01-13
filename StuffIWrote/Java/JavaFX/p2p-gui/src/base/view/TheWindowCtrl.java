package base.view;

import base.Main;
import base.p2p.file.FakeP2PFile;
import base.p2p.file.P2PFile;
import base.p2p.tracker.FakeTracker;
import base.p2p.transfer.Transfer;
import base.util.TreeTableRoot;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.controlsfx.dialog.Dialogs;

import java.io.File;
import java.time.LocalDate;

/**
 * Ethan Petuchowski 1/7/15
 *
 * This one has the file-list scene on the top half, and multiple
 * options for scenes on the bottom half
 *  (e.g. bandwidth usage history, peer list, etc.)
 */
public class TheWindowCtrl {

    /* this allows the FileChooser to access the main-stage */
    private Main main;
    public void setMain(Main main) { this.main = main; }

    /*************************************************************************
     *  TODO just PUT SAMPLE ITEMS in all the places where items should be.  *
     *  Although actually, this should be done inside the Main() CONSTRUCTOR *
     *************************************************************************/

    // automatically called after the fxml file is loaded
    @FXML private void initialize() {
        initializeLocalFileTable();
        initializeTrkrTreeTable();
        addFakeContent();
    }

    private void initializeLocalFileTable() {
        localFileTable.setEditable(false);
        localFileTable.setRowFactory(tv -> {
            TableRow<P2PFile> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    System.out.println("right clicked");
                    event.consume();
                    cm.show(stage, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        });
        localFileNameColumn.setCellValueFactory(new PropertyValueFactory<>("filename"));
        // not sure if there's a better way to write this
        localFileSizeColumn.setCellValueFactory(cellData ->
            new ReadOnlyObjectWrapper<>(cellData.getValue().getFilesizeString()));

        percentCompleteColumn.setCellValueFactory(cellData ->
            new ReadOnlyObjectWrapper<>(cellData.getValue().getCompletenessString()));

        // TODO this says "when a selection is made in the fileTable, do whatever I want"
        // at this point, I'm not doing anything
        localFileTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> fileWasSelected(newValue));

        cm.getItems().add(cmItem1);
    }

    private final ContextMenu cm = new ContextMenu();
    MenuItem cmItem1 = new MenuItem("Copy Image");

    private void initializeTrkrTreeTable() {
        treeTable.setRoot(treeTableRoot);

        // Defining how cell content is extracted from each SwarmTreeRenderable for each column
        treeNameCol.setCellValueFactory(

                // option 1: using a provided property value-getting class
                new TreeItemPropertyValueFactory<>("name"));

                // option 2: using a read-only 'property' created on-the-fly
//                p -> new ReadOnlyStringWrapper(p.getValue().getValue().getName()));

        treeSizeCol.setCellValueFactory(
                p -> new ReadOnlyStringWrapper(p.getValue().getValue().getSizeString()));

        treeSeedersCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("numSeeders"));
        treeLeechersCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("numLeechers"));

        treeTable.setShowRoot(true); // show the "'root' tree-item"
    }

    private void addFakeContent() {
        FakeP2PFile local1 = FakeP2PFile.genFakeFile();
        FakeP2PFile local2 = FakeP2PFile.genFakeFile();
        FakeP2PFile local3 = FakeP2PFile.genFakeFile();
        localFileTable.getItems().addAll(local1, local2, local3);

        // Add the defaultFakeTracker as a child of the Tree's root
        treeTableRoot.addTracker(FakeTracker.getDefaultFakeTracker());
    }

    private void fileWasSelected(P2PFile p2pFile) { if (p2pFile != null) {} else {} }

    /** UPPER PANES **/

    /* It looks like this:
     * ===========================================================
     * ||   Tracker/FileName       ||  Size     || #Sd  || #Lch ||
     * -----------------------------------------------------------
     * || \/ Trackers              || 2 trackers||      ||      ||
     * ||   \/ 123.tra.ker.adr:523 || 2 files   ||      ||      ||
     * ||       file1              ||  size1 B  ||  23  ||  31  ||
     * ||       file2              ||  size2 B  ||   6  ||   2  ||
     * ||   \/ 213.tra.ker.adr:412 || 2 files   ||      ||      ||
     * ||       file3              ||  size1 B  ||  21  ||  11  ||
     * ||       file4              ||  size2 B  ||   7  ||   4  ||
     * ||    ...                   ||  ...      || ...  || ...  ||
     * ===========================================================
     */

    SwarmTreeItem treeTableRoot = new SwarmTreeItem(new Celery(new TreeTableRoot()));
    @FXML private TreeTableColumn<Celery,Celery> treeNameCol;
    @FXML private TreeTableColumn<Celery,Celery> treeSizeCol;
    @FXML private TreeTableColumn<Celery,Celery> treeSeedersCol;
    @FXML private TreeTableColumn<Celery,Celery> treeLeechersCol;

    @FXML private TreeTableView<Celery> treeTable;

    @FXML private TableView<P2PFile> localFileTable;
    // type1 == type of TableView, type2 == type of cell content
    @FXML private TableColumn<P2PFile,String> localFileNameColumn;
    @FXML private TableColumn<P2PFile,String> localFileSizeColumn;
    @FXML private TableColumn<P2PFile,String> percentCompleteColumn;

    /** LOWER PANES **/
    /** Transfers (progress bars) **/

    @FXML private TableView<Transfer> transfersForSelectedFile;
    @FXML private TableColumn<Transfer,Integer> transferChunkNumCol;
    @FXML private TableColumn<Transfer,ProgressBar> transferProgressCol;

    /** Bandwidth History (graph) **/
    @FXML private LineChart<LocalDate, Integer> bandwidthHistoryLineChart;

    // TODO in TheWindow this is a "category axis" which doesn't really sound like what I want
    @FXML private Axis<LocalDate> bandwidthChartXAxis;
    @FXML private Axis<Integer> bandwidthChartYAxis;

    /** Pieces Diagram **/
    // haven't figured out exactly what this *is* yet.


    /** MENU BAR ITEMS **/

    @FXML private void addFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(Main.getPrimaryStage());
        if (file != null) {
            // TODO turn into P2PFile and add file to TableView<P2PFile> fileList
            // or maybe add it to the ObservableList<P2PFile> localFiles
            // and the table view will "listen" for it
        }
    }
    @FXML private void addTracker() {
        // TODO make a Dialogs for entering in an IPAddress + Port
        // and turn that into a Tracker object and add it to
        // ObservableList<Tracker> knownTrackers
    }
    @FXML private void aboutDialog() {
        // TODO put a real link to the source in the dialog
        Dialogs.create()
               .title("p2p-gui")
               .masthead("About") // this must be that '(i)' icon thing
               .message("By Ethan Petuchowski\n"+
                        "github.com/ethanp/p2p-gui\n"+
                        "December 2015")
               .showInformation();
    }
    @FXML private void quit() { System.exit(0); }

}
