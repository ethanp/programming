package nongeneric;

import toret.Child;

/**
 * Ethan Petuchowski 1/15/15
 */
public class NongenericChild extends NongenericParent {
    protected Child inst = new Child();

    @Override public String toString() {
        return "NongenericChild{"+
               "inst="+inst+
               '}';
    }

    @Override public Child getInst() {
        return inst;
    }

    public void setInst(Child inst) {
        this.inst = inst;
    }
}
