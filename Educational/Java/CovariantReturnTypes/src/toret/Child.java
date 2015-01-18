package toret;

/**
 * Ethan Petuchowski 1/15/15
 */
public class Child extends Parent {
    protected int bInt = 5;

    @Override public String toString() {
        return "Child{"+
               "bInt="+bInt+
               '}';
    }

    @Override public void incr() {
        bInt++;
    }

    public int getbInt() {
        return bInt;
    }

    public void setbInt(int bInt) {
        this.bInt = bInt;
    }
}
