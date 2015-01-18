package generic;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import toret.Child;

/**
 * Ethan Petuchowski 1/15/15
 */
public class GenericChild extends GenericParent {
    protected ObjectProperty<Child> inst
            = new SimpleObjectProperty<>(new Child());

    @Override public String toString() {
        return "GenericChild{"+
               "inst="+inst+
               '}';
    }

    @Override public void incr() {
        inst.getValue().incr();
    }

    @Override public Child getInst() {
        return inst.get();
    }

    public void setInst(Child inst) {
        this.inst.set(inst);
    }
}
