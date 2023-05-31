package co.lemnisk.consumer.exception;

public class InvalidConfigurationException extends RuntimeException {

    private static final long serialVersionUID = 7308146117450473949L;

    public InvalidConfigurationException() {
        super();
    }

    public InvalidConfigurationException(final String message) {
        super(message);
    }

    public InvalidConfigurationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public InvalidConfigurationException(final Throwable throwable) {
        super(throwable);
    }

}