package core.commands.exceptions;

/**
 * @author Артур Куприянов
 */
public class CommandHasNoNameException extends CommandException {
    public CommandHasNoNameException(String msg) {
        super(msg);
    }

}
