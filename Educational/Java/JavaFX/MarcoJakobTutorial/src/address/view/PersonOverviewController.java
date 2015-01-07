package address.view;

import address.MainApp;
import address.model.Person;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Ethan Petuchowski 1/6/15
 */
public class PersonOverviewController {

    @FXML private TableView<Person> personTable;
    @FXML private TableColumn<Person, String> firstNameColumn;
    @FXML private TableColumn<Person, String> lastNameColumn;

    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label streetLabel;
    @FXML private Label postalCodeLabel;
    @FXML private Label cityLabel;
    @FXML private Label birthdayLabel;

    // Reference to the main application
    private MainApp mainApp;

    // this constructor is called before the initialize() method below
    public PersonOverviewController() {}

    // automatically called after the fxml file is loaded
    @FXML private void initialize() {
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // add items to table from mainApp's *field*   personData : ObservableList<Person>
        personTable.setItems(mainApp.getPersonData());
    }

}
