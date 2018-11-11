package core.commands.exceptions;

/**
 * @author Артур Куприянов
 */
public abstract class CommandException extends NullPointerException {
    public CommandException(String msg){super(msg);}
}
