package core.modules.queuev2.exceptions;

/**
 * @author Arthur Kupriyanov
 */
public abstract class QueueException extends RuntimeException{
    public QueueException() {
        super();
    }

    public QueueException(String message) {
        super(message);
    }

    protected QueueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
