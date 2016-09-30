package scrabble.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class ViewController {
    @FXML private MenuItem closeButton;

    @FXML public void closeApp(ActionEvent event) {
        System.exit(0);
    }
}
