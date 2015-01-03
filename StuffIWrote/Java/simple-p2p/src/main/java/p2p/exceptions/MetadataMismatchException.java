package p2p.exceptions;

/**
 * Ethan Petuchowski 1/2/15
 */
public class MetadataMismatchException extends P2PException {
    public MetadataMismatchException() {
        super();
    }
    public MetadataMismatchException(String message) {
        super(message);
    }
    public MetadataMismatchException(Throwable cause) {
        super(cause);
    }
    public MetadataMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
