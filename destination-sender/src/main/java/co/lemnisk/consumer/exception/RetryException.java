package co.lemnisk.consumer.exception;

public class RetryException extends RuntimeException {

    private static final long serialVersionUID = 7581693574601394251L;

    public RetryException() {
        super();
    }

    public RetryException(final String message) {
        super(message);
    }

    public RetryException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public RetryException(final Throwable throwable) {
        super(throwable);
    }

}
