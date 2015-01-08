package base.view;

import base.Main;
import base.p2p.file.P2PFile;
import base.p2p.tracker.Tracker;
import base.p2p.transfer.Transfer;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableView;
import javafx.stage.FileChooser;
import org.controlsfx.dialog.Dialogs;

import java.io.File;

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
    @FXML private void initialize() {}


    /** MENU BAR ITEMS **/

    @FXML private void addFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(main.getPrimaryStage());
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

    /** UPPER PANES **/

    // TODO sync this with Main's ObservableList<Tracker> knownTrackers;
    @FXML private TreeTableView<Tracker> trackerList;

    // TODO sync this with Main's ObservableList<P2PFile> localFiles;
    @FXML private TableView<P2PFile> fileList;

    /** LOWER PANES **/
    /** Transfers (progress bars) **/

    // TODO sync this with Main's ObservableList<Transfer> ongoingTransfers;
    @FXML private TableView<Transfer> transfersForSelectedFile;

    /** Bandwidth History (graph) **/
    /** Pieces Diagram **/

}
