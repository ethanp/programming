package util;

/**
 * Ethan Petuchowski 3/2/16
 */
public class SingleLLNode {
    public char val;
    public SingleLLNode fwd;

    public SingleLLNode(char val) {
        this.val = val;
    }

    public SingleLLNode(char val, SingleLLNode fwd) {
        this.val = val;
        this.fwd = fwd;
    }

    public String listString() {
        return val + (fwd == null ? "" : fwd.listString());
    }
}
