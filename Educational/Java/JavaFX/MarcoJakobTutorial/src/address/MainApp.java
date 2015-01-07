package address;

import address.model.Person;
import address.model.PersonListWrapper;
import address.view.BirthdayStatisticsController;
import address.view.PersonEditDialogController;
import address.view.PersonOverviewController;
import address.view.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;


    private ObservableList<Person> personData = FXCollections.observableArrayList();

    public ObservableList<Person> getPersonData() { return personData; }

    public MainApp() {
        // add sample data
        personData.add(new Person("Hans", "Muster"));
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Heinz", "Kurt"));
        personData.add(new Person("Cornelia", "Meier"));
        personData.add(new Person("Werner", "Meyer"));
        personData.add(new Person("Lydia", "Koontz"));
        personData.add(new Person("Anna", "Best"));
        personData.add(new Person("Stefan", "Meier"));
        personData.add(new Person("Martin", "Mueller"));
    }

    @Override public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");

        initRootLayout();

        showPersonOverview();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // give the Controller access to the mainApp
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // try to load the last opened person file
        File file = getPersonFilePath();
        if (file != null)
            loadPersonDataFromFile(file);
    }

    public void showPersonOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));

            AnchorPane personOverview = loader.load();
            rootLayout.setCenter(personOverview);

            PersonOverviewController controller = loader.getController();
            controller.setMainApp(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return true iff user clicked OK and changes can be saved into the person object
     */
    public boolean showPersonEditDialog(Person person) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
            AnchorPane page = loader.load();

            // create window for popup
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // set the person
            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            // dialog until closed by user (blocks current event
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @return file last opened, read from OS-specific registry; null if not found
     */
    public File getPersonFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null)
            return new File(filePath);
        else
            return null;
    }

    /**
     * set file path of currently loaded file; persisted in OS-specific registry
     * pass null to clear the path
     */
    public void setPersonFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());
            primaryStage.setTitle("AddressApp - "+file.getName());
        }
        else {
            prefs.remove("filePath");
            primaryStage.setTitle("AddressApp");
        }
    }

    public void loadPersonDataFromFile(File file) {
        try {

            /* I think this thing reads the schema(s) available from the given loc */
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);

            /* an Unmarshaller converts XML data into a tree of Java content objects */
            Unmarshaller um = context.createUnmarshaller();

            /* we create an instance of a PersonListWrapper
               from the XML data contained in the given File */
            PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

            personData.clear();
            personData.addAll(wrapper.getPersons());

            setPersonFilePath(file);
        }
        catch (Exception e) {
            Dialogs.create()
                   .title("Error")
                   .masthead("Could not load data from file:\n"+file.getPath())
                   .showException(e);
        }
    }

    public void savePersonDataToFile(File file) {
        try {

            /* read the JAXB schema specified in the wrapper class */
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);

            /* use the schema to create a Marshaller */
            Marshaller m = context.createMarshaller();

            /* do format the output with linefeeds and indentation */
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            /* create a JAXB wrapper */
            PersonListWrapper wrapper = new PersonListWrapper();

            /* stick a person in it */
            wrapper.setPersons(personData);

            /* convert him to XML */
            m.marshal(wrapper, file);

            /* save this location in the app's Preferences object */
            setPersonFilePath(file);
        }
        catch (Exception e) {
            Dialogs.create()
                   .title("Error")
                   .masthead("Could not save data to file:\n"+file.getPath())
                   .showException(e);
        }
    }

    /** open a dialog that shows birthday statistics */
    public void showBirthdayStatistics() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BirthdayStatistics.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Birthday Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            BirthdayStatisticsController controller = loader.getController();
            controller.setPersonData(personData);
            dialogStage.show();
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public Stage getPrimaryStage() { return primaryStage; }
    public static void main(String[] args) { launch(args); }
}
