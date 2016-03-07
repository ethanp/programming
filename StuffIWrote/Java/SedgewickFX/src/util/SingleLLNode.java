package util;

/**
 * Ethan Petuchowski 3/2/16
 */
public class SingleLLNode<T> {
    public T val;
    public SingleLLNode<T> nxt;

    public SingleLLNode(T val) {
        this.val = val;
    }

    public SingleLLNode(T val, SingleLLNode<T> nxt) {
        this.val = val;
        this.nxt = nxt;
    }

    public String listString() {
        return String.valueOf(val) + (nxt == null ? "" : nxt.listString());
    }

    @Override public String toString() {
        return String.valueOf(val);
    }
}
