package co.lemnisk.consumer.exception;

public class BadEventException extends RuntimeException {

    private static final long serialVersionUID = 7581693574601394251L;

    public BadEventException() {
        super();
    }

    public BadEventException(final String message) {
        super(message);
    }

    public BadEventException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public BadEventException(final Throwable throwable) {
        super(throwable);
    }

}
