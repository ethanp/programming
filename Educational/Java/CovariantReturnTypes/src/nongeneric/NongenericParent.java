package nongeneric;

import toret.Parent;

/**
 * Ethan Petuchowski 1/15/15
 */
public class NongenericParent {
    protected Parent inst = new Parent();

    @Override public String toString() {
        return "NongenericParent{"+
               "inst="+inst+
               '}';
    }

    public void incr() {
        inst.incr();
    }

    public Parent getInst() {
        return inst;
    }

    public void setInst(Parent inst) {
        this.inst = inst;
    }
}
