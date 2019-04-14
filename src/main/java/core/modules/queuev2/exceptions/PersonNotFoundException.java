package core.modules.queuev2.exceptions;

/**
 * Выбрасывается, когда не найден персонаж
 *
 * Не заполняет stack trace
 *
 * @author Arthur Kupriyanov
 */
public class PersonNotFoundException extends QueueException {

    public PersonNotFoundException() {
        super();
    }

    public PersonNotFoundException(String message) {
        super(message, new PermissionDeniedException(), true, false);
    }
}
