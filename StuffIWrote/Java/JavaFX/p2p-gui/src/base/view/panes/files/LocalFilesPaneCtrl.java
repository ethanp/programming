package base.view.panes.files;

import base.p2p.file.P2PFile;
import javafx.beans.property.ReadOnlyObjectWrapper;
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

    @FXML private void initialize() {
        localFileTable.setEditable(false);
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

    }
    private void fileWasSelected(P2PFile p2pFile) { if (p2pFile != null) {} else {} }
}
