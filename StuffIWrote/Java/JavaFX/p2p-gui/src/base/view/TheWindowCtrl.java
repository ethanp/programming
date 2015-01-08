package base.view;

import base.Main;
import base.p2p.file.P2PFile;
import base.p2p.tracker.Tracker;
import base.p2p.transfer.Transfer;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableView;
import javafx.stage.FileChooser;

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


    /** MENU BAR ITEMS **/

    @FXML private void addFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(main.getPrimaryStage());
        if (file != null) {
            // TODO turn into P2PFile and add file to TableView<P2PFile> fileList
        }
    }
    @FXML private void addTracker() {}
    @FXML private void showAbout() {}
    @FXML private void quit() { System.exit(0); }

    /** UPPER PANES **/
    @FXML private TreeTableView<Tracker> trackerList;
    @FXML private TableView<P2PFile> fileList;

    /** LOWER PANES **/
    /** Transfers (progress bars) **/
    @FXML private TableView<Transfer> transfersForSelectedFile;

    /** Bandwidth History (graph) **/
    /** Pieces Diagram **/

    // automatically called after the fxml file is loaded
    @FXML private void initialize() {}
}
