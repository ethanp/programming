package base.view.panes.files;

import base.Main;
import base.p2p.file.P2PFile;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Ethan Petuchowski 1/9/15
 */
public class LocalFilesPaneCtrl {

    @FXML private TableView<P2PFile> localFileTable;
    @FXML private TableColumn<P2PFile,String> localFileNameColumn;
    @FXML private TableColumn<P2PFile,String> localFileSizeColumn;
    @FXML private TableColumn<P2PFile,String> percentCompleteColumn;

    private final ListChangeListener<P2PFile> localFilesListener = c -> {
        while (c.next()) {
            if (c.wasRemoved()) {
                for (P2PFile f : c.getRemoved()) {
                    P2PFile r = null;
                    for (P2PFile m : localFileTable.getItems()) {
                        if (f.equals(m)) {
                            r = m;
                            break;
                        }
                    }
                    if (r != null) {
                        localFileTable.getItems().remove(r);
                    }
                }
            }
            if (c.wasAdded()) {
                for (P2PFile f : c.getAddedSubList()) {
                    localFileTable.getItems().add(f);
                }
            }
        }
    };

    @FXML private void initialize() {
        localFileTable.setEditable(false);
        localFileTable.getItems().addAll(Main.localFiles);
        Main.localFiles.addListener(localFilesListener);
        localFileNameColumn.setCellValueFactory(new PropertyValueFactory<>("filename"));
        localFileSizeColumn.setCellValueFactory(cellData ->
            new ReadOnlyObjectWrapper<>(cellData.getValue().getFilesizeString()));

        percentCompleteColumn.setCellValueFactory(cellData ->
            new ReadOnlyObjectWrapper<>(cellData.getValue().getCompletenessString()));

        /* this says "when a selection is made in the fileTable, do whatever I want"
         * at this point, I'm not doing anything */
        localFileTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> fileWasSelected(newValue));

    }
    private void fileWasSelected(P2PFile p2pFile) {
        // TODO show THIS file's 'Piece Progress' and 'Download Viz'
        if (p2pFile != null) {} else {}
    }
}
