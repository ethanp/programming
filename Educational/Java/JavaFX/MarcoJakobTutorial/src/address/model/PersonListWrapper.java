package address.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Ethan Petuchowski 1/7/15
 *
 * This is for getting JAXB to marshal our List<Person> into XML
 */
@XmlRootElement(name = "persons")
public class PersonListWrapper {

    private List<Person> persons;

    @XmlElement(name = "person") public List<Person>
    getPersons() { return persons; }

    public void setPersons(List<Person> persons) { this.persons = persons; }
}
