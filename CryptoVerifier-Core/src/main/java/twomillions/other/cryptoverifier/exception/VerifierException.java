package twomillions.other.cryptoverifier.exception;

@SuppressWarnings("unused")
public class VerifierException extends Exception {
    public VerifierException() {
        super();
    }

    public VerifierException(String message) {
        super(message);
    }

    public VerifierException(String message, Throwable cause) {
        super(message, cause);
    }

    public VerifierException(Throwable cause) {
        super(cause);
    }
}