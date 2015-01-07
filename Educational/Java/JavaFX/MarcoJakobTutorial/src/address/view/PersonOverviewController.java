package address.view;

import address.MainApp;
import address.model.Person;
import address.util.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.controlsfx.dialog.Dialogs;

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

        // load people index (left table pane)
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        // clear person details (right table pane)
        showPersonDetails(null);

        // listen for selection changes, and show details of person selected
        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // add items to table from mainApp's *field*   personData : ObservableList<Person>
        personTable.setItems(mainApp.getPersonData());
    }

    /** fill text fields showing details of person; clear all for null person */
    private void showPersonDetails(Person person) {
        if (person != null) {
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
            cityLabel.setText(person.getCity());
            birthdayLabel.setText(DateUtil.format(person.getBirthday())); // DateUtil is our class
        } else {
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }
    }

    @FXML private void handleDeletePerson() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            personTable.getItems().remove(selectedIndex);
        } else {
            // nothing was selected (idx == -1)
            Dialogs.create()
                   .title("No Selection")
                   .masthead("No Person Selected")
                   .message("Please select a person in the table.")
                   .showWarning();
        }
    }

    /** open dialog to edit details for a new Person */
    @FXML private void handleNewPerson() {
        Person tempPerson = new Person();
        boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
        if (okClicked) {
            mainApp.getPersonData().add(tempPerson);
        }
    }

    /** open dialog to edit selected Person */
    @FXML private void handleEditPerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
            if (okClicked)
                showPersonDetails(selectedPerson);
        }
        else {
            Dialogs.create()
                   .title("No Selection")
                   .masthead("No Person Selected")
                   .message("Please select a person in the table.")
                   .showWarning();
        }
    }
}
