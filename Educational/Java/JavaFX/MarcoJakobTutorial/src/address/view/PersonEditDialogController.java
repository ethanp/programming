package address.view;

import address.model.Person;
import address.util.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;

/**
 * Ethan Petuchowski 1/6/15
 */
public class PersonEditDialogController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField streetField;
    @FXML private TextField postalCodeField;
    @FXML private TextField cityField;
    @FXML private TextField birthdayField;

    /* note: the PersonOverviewController does NOT have its own Stage
     *       this one needs one because a Stage is a Window, and this
     *       Dialog is a POPUP
     */
    private Stage dialogStage;
    private Person person;

    private boolean okClicked = false;
    public boolean isOkClicked() { return okClicked; }

    /** automatically called after fxml file has been loaded */
    @FXML private void initialize() {}

    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }

    public void setPerson(Person person) {
        this.person = person;

        firstNameField.setText(person.getFirstName());
        lastNameField.setText(person.getLastName());
        streetField.setText(person.getStreet());
        postalCodeField.setText(Integer.toString(person.getPostalCode()));
        cityField.setText(person.getCity());
        birthdayField.setText(DateUtil.format(person.getBirthday())); // DateUtil is our class
        birthdayField.setPromptText("mm/dd/yyy");
    }

    @FXML private void handleOk() {
        if (isInputValid()) {
            person.setFirstName(firstNameField.getText());
            person.setLastName(lastNameField.getText());
            person.setStreet(streetField.getText());
            person.setPostalCode(Integer.parseInt(postalCodeField.getText()));
            person.setCity(cityField.getText());
            person.setBirthday(DateUtil.parse(birthdayField.getText()));

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML private void handleCancel() { dialogStage.close(); }

    private boolean isInputValid() {
        String errorMessage = "";
        if (firstNameField.getText() == null || firstNameField.getText().length() == 0)
            errorMessage += "No valid first name!\n";
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0)
            errorMessage += "No valid last name!\n";
        if (streetField.getText() == null || streetField.getText().length() == 0)
            errorMessage += "No valid street!\n";

        if (postalCodeField.getText() == null || postalCodeField.getText().length() == 0)
            errorMessage += "No valid postal code!\n";
        else
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(postalCodeField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid postal code (must be an integer)!\n";
            }

        if (cityField.getText() == null || cityField.getText().length() == 0)
            errorMessage += "No valid city!\n";
        if (birthdayField.getText() == null || birthdayField.getText().length() == 0)
            errorMessage += "No valid birthday!\n";
        else if (!DateUtil.validDate(birthdayField.getText()))
            errorMessage += "No valid birthday. Use the format dd.mm.yyyy!\n";

        if (errorMessage.length() == 0)
            return true;
        else
            // Show the error message.
            Dialogs.create()
                .title("Invalid Fields")
                .masthead("Please correct invalid fields")
                .message(errorMessage)
                .showError();
            return false;
    }
}
