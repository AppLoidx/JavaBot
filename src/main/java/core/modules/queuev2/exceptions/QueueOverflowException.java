package core.modules.queuev2.exceptions;

/**
 * Переполнение очереди
 * Не заполняет stack-trace
 *
 * @author Arthur Kupriyanov
 */
public class QueueOverflowException extends QueueException{
    public QueueOverflowException(){
        super();
    }
    public QueueOverflowException(String message) {
        super(message, new QueueOverflowException(message), false, false);
    }
}
