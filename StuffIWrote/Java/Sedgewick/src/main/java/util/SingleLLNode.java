package util;

/**
 * Ethan Petuchowski 3/2/16
 */
public class SingleLLNode {
    public char val;
    public SingleLLNode nxt;

    public SingleLLNode(char val) {
        this.val = val;
    }

    public SingleLLNode(char val, SingleLLNode nxt) {
        this.val = val;
        this.nxt = nxt;
    }

    public String listString() {
        return val + (nxt == null ? "" : nxt.listString());
    }

    @Override public String toString() {
        return String.valueOf(val);
    }
}
