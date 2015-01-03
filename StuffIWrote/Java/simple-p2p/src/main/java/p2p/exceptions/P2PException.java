package p2p.exceptions;

/**
 * Ethan Petuchowski 1/2/15
 */
public class P2PException extends Exception {
    public P2PException() {
        super();
    }
    public P2PException(String message) {
        super(message);
    }
    public P2PException(Throwable cause) {
        super(cause);
    }
    public P2PException(String message, Throwable cause) {
        super(message, cause);
    }
}
