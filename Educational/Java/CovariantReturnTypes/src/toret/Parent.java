package toret;

/**
 * Ethan Petuchowski 1/15/15
 */
public class Parent {
    protected int aInt = 0;

    @Override public String toString() {
        return "Parent{"+
               "aInt="+aInt+
               '}';
    }

    public void incr() {
        aInt++;
    }

    public int getaInt() {
        return aInt;
    }

    public void setaInt(int aInt) {
        this.aInt = aInt;
    }
}
