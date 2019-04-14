package core.modules.queuev2.exceptions;

/**
 * @author Arthur Kupriyanov
 */
public class PersonAlreadyExistException extends QueueException {
    public PersonAlreadyExistException(){
        super();
    }
    public PersonAlreadyExistException(String message) {
        super(message, new PersonAlreadyExistException(), false, false);
    }
}
