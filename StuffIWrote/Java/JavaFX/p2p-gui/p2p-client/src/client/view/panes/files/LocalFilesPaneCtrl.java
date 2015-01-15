package client.view.panes.files;

import client.Main;
import p2p.file.P2PFile;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.util.Arrays;
import java.util.List;

/**
 * Ethan Petuchowski 1/9/15
 */
public class LocalFilesPaneCtrl {

    @FXML private TableView<P2PFile> localFileTable;
    @FXML private TableColumn<P2PFile,P2PFile> nameCol;
    @FXML private TableColumn<P2PFile,P2PFile> sizeCol;
    @FXML private TableColumn<P2PFile,P2PFile> percentCol;

    Callback<TableColumn.CellDataFeatures<P2PFile, P2PFile>,ObservableValue<P2PFile>>
                valueFactory = cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue());

    private List<TableColumn<P2PFile,P2PFile>> tableColumns;

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
        tableColumns = Arrays.asList(nameCol, sizeCol, percentCol);
        tableColumns.stream().forEach(c -> c.setCellValueFactory(valueFactory));

        nameCol.setCellFactory(e -> new LocalFileCell(LocalFileCell.Col.NAME));
        sizeCol.setCellFactory(e -> new LocalFileCell(LocalFileCell.Col.SIZE));
        percentCol.setCellFactory(e -> new LocalFileCell(LocalFileCell.Col.PERCENT));

        // this says "when a selection is made in the fileTable, do whatever I want"
        localFileTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> fileWasSelected(newValue));

    }
    private void fileWasSelected(P2PFile p2pFile) {
        // TODO show THIS file's 'Piece Progress' and 'Download Viz'
        if (p2pFile != null) {} else {}
    }
}
