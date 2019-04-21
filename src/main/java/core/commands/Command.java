package core.commands;

import core.commands.exceptions.CommandHasNoNameException;

/**
 * Abstract class for all executable classes-commands
 * Field {@link #name} identification command,he is called by this name
 *
 * @author Arthur Kupriyanov
 * @version 1.1
 */
public abstract class Command {

    private final String name;
    protected String commandName;
    {
        setConfig();
        name = commandName;
    }
    /**
     * Command initialization
     *
     * @return response
     */
    public String init(String ... args){
        return null;
    }

    protected abstract void setConfig();

    public final String init(String params){
        return init(params.split(" "));
    }


    /**
     * Get the name of the command by which it will be called
     *
     * @return command name
     * @throws CommandHasNoNameException Not assigned a field <code>{@link #name}</code>
     */
    public final String getName() throws CommandHasNoNameException {
        if (name == null){
            throw new CommandHasNoNameException(String.format("Команда %s не имеет имени", this.getClass()));
        }

        return name;
    }


    /**
     * Возвращает строку в формате:<br>
     * name: имяКоманды<br>
     *
     * @return форматированное имя и мод команды
     */
    @Override
    public String toString() {
        return String.format("name: %s",this.getName());
    }


    /**
     * Берет хэш-код значащего поля {@link #name}
     *
     * @return хэш-код команды
     */
    @Override
    public int hashCode() {
        if (this.name == null){
            throw new CommandHasNoNameException("Команда не имеет имени!");
        }

        return this.name.hashCode();
    }


    /**
     * Объекты эквивалентны только, если поля <code>{@link #name}</code> равны
     * имеют одинаковое значение и объект является классом-наследником {@link Command}
     * @param obj сравниваемый объект
     * @return {@code true} если объекты эквивалентны; {@code false} если объекты различаются
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Command){
            if (getName().equals(((Command) obj).getName())){
                return true;
            }
        }
        return false;
    }
}
