package core.modules.queue.exceptions;

/**
 * @author Arthur Kupriyanov
 */
public class QueueOverflowException extends UnsupportedOperationException {
    public QueueOverflowException(String msg){
        super(msg);
    }

}
