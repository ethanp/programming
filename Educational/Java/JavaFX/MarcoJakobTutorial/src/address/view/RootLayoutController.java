package address.view;

import address.MainApp;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import org.controlsfx.dialog.Dialogs;

import java.io.File;

/**
 * Ethan Petuchowski 1/7/15
 */
public class RootLayoutController {

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) { this.mainApp = mainApp; }

    /** create empty address book */
    @FXML private void handleNew() {
        mainApp.getPersonData().clear();
        mainApp.setPersonFilePath(null);
    }

    /** use FileChooser to let user select an address book to load */
    @FXML private void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if (file != null)
            mainApp.loadPersonDataFromFile(file);
    }

    /** save to open file, or show "save as" if none is open */
    @FXML private void handleSave() {
        File personFile = mainApp.getPersonFilePath();
        if (personFile != null)
            mainApp.savePersonDataToFile(personFile);
        else
            handleSaveAs();
    }

    /** use FileChooser to let user select a file to save to */
    @FXML private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        if (file != null) {
            if (!file.getPath().endsWith(".xml"))
                file = new File(file.getPath()+".xml"); // not sure I like this route...
            mainApp.savePersonDataToFile(file);
        }
    }

    /** open an "About" dialog */
    @FXML private void handleAbout() {
        Dialogs.create()
               .title("AddressApp")
               .masthead("About")
               .message("Author: Marco Jakob\n"+
                        "Website: http://code.makery.ch\n"+
                        "code typed in by Ethan Petuchowski, thank you very much")
               .showInformation();
    }

    /** quit the applciation */
    @FXML private void handleExit() { System.exit(0); }
}
