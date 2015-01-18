package generic;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import toret.Parent;

/**
 * Ethan Petuchowski 1/15/15
 */
public class GenericParent {
    protected ObjectProperty<Parent> inst
            = new SimpleObjectProperty<>(new Parent());

    @Override public String toString() {
        return "GenericParent{"+
               "inst="+inst+
               '}';
    }

    public void incr() {
        inst.getValue().incr();
    }

    public Parent getInst() {
        return inst.get();
    }

    public ObjectProperty<Parent> instProperty() {
        return inst;
    }

    public void setInst(Parent inst) {
        this.inst.set(inst);
    }
}
