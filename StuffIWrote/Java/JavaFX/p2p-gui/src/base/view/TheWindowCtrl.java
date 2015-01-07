package base.view;

import base.Main;
import javafx.fxml.FXML;

/**
 * Ethan Petuchowski 1/7/15
 *
 * this one just has the
 */
public class TheWindowCtrl {

    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    // automatically called after the fxml file is loaded
    @FXML private void initialize() {}
}
