package base.view;

import base.Main;
import base.p2p.file.FakeP2PFile;
import base.p2p.transfer.Transfer;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
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

    // automatically called after the fxml file is loaded
    @FXML private void initialize() {
    }

    private void addFakeContent() {
        FakeP2PFile local1 = FakeP2PFile.genFakeFile();
        FakeP2PFile local2 = FakeP2PFile.genFakeFile();
        FakeP2PFile local3 = FakeP2PFile.genFakeFile();
//        localFileTable.getItems().addAll(local1, local2, local3);

        // Add the defaultFakeTracker as a child of the Tree's root
//        treeTableRoot.addTracker(FakeTracker.getDefaultFakeTracker());
    }


    /** UPPER PANES **/
    @FXML public AnchorPane trackersAnchor;
    @FXML public AnchorPane localAnchor;

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
