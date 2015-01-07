package address.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

/**
 * Ethan Petuchowski 1/6/15
 */
public class Person {

    public String getFirstName() { return firstName.get(); }
    public StringProperty firstNameProperty() { return firstName; }
    public void setFirstName(String firstName) { this.firstName.set(firstName); }
    public String getLastName() { return lastName.get(); }
    public StringProperty lastNameProperty() { return lastName; }
    public void setLastName(String lastName) { this.lastName.set(lastName); }
    public String getStreet() { return street.get(); }
    public StringProperty streetProperty() { return street; }
    public void setStreet(String street) { this.street.set(street); }
    public int getPostalCode() { return postalCode.get(); }
    public IntegerProperty postalCodeProperty() { return postalCode; }
    public void setPostalCode(int postalCode) { this.postalCode.set(postalCode); }
    public String getCity() { return city.get(); }
    public StringProperty cityProperty() { return city; }
    public void setCity(String city) { this.city.set(city); }
    public LocalDate getBirthday() { return birthday.get(); }
    public ObjectProperty<LocalDate> birthdayProperty() { return birthday; }
    public void setBirthday(LocalDate birthday) { this.birthday.set(birthday); }

    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty street;
    private final IntegerProperty postalCode;
    private final StringProperty city;
    private final ObjectProperty<LocalDate> birthday; /* Compat: LocalDate is JDK 8 */

    public Person() {
        this(null, null);
    }

    public Person(String firstName, String lastName) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);

        // dummy data for testing
        this.street = new SimpleStringProperty("some street");
        this.postalCode = new SimpleIntegerProperty(1234);
        this.city = new SimpleStringProperty("some city");
        this.birthday = new SimpleObjectProperty<LocalDate>(LocalDate.of(1999, 2, 21));
    }
}
