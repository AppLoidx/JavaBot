package core.modules.queuev2.exceptions;

/**
 * Выбрасывается, когда операция недопустима (нет разрешения)
 *
 * Не заполняет stack trace
 *
 * @author Arthur Kupriyanov
 */
public class PermissionDeniedException extends QueueException{

    public PermissionDeniedException(){
        super();
    }
    public PermissionDeniedException(String message) {
        super(message, new PermissionDeniedException(), true, false);
    }
}
