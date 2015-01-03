package p2p.exceptions;

/**
 * Ethan Petuchowski 1/2/15
 */
public class SwarmNotFoundException extends P2PException {
    public SwarmNotFoundException() {
        super();
    }
    public SwarmNotFoundException(String message) {
        super(message);
    }
    public SwarmNotFoundException(Throwable cause) {
        super(cause);
    }
    public SwarmNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
