package core.commands;

import core.commands.enums.Mode;
import core.commands.exceptions.CommandHasNoModeException;
import core.commands.exceptions.CommandHasNoNameException;

/**
 * Абстрактный класс для всех исполняемых команд
 *
 * Необходимо всегда определять поля <code>{@link #name}</code> и <code>{@link #mode}</code>
 *
 * @author Артур Куприянов
 * @version 1.0
 */
public abstract class Command {
    protected String name = null;
    protected Mode mode = null;

    private static int counter = 0;
    private int hashCode;

    {
        hashCode = counter++;
    }

    /**
     * Метод реализации команды
     *
     * @return Строку, возвращаемую пользователю
     */
    public abstract String init(String ... args);

    public String init(String params){
        return init(params.split(" "));
    }

    /**
     * Получение мода команды, в которой она выполняется
     *
     * @return <code>{@link Mode}</code> объект команды
     * @see Mode
     * @throws CommandHasNoModeException Не присвоено значение поля <code>{@link #mode}</code>
     */
    public final Mode getMode() throws CommandHasNoModeException {
        if (mode == null){
            throw new CommandHasNoModeException(String.format("Команда %s не имеет мода", this.getClass()));
        }

        return mode;
    }


    /**
     * Получение имени команды, по которой она будет вызываться
     *
     * @return Имя команды
     * @throws CommandHasNoNameException -- Не присвоено значение поля <code>{@link #name}</code>
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
     * mode: модКоманды
     *
     * @return форматированное имя и мод команды
     */
    @Override
    public String toString() {
        return String.format("name: %s\nmode: %s",this.getName(), this.getMode());
    }


    /**
     * <code>{@link #hashCode}</code> определяется в блоке инициализации и принимает значение <code>{@link #counter}</code>
     * Таким образом хэш-код всегда будет разным для команд
     *
     * @return хэш-код команды
     */
    @Override
    public int hashCode() {
        return hashCode;
    }


    /**
     * Объекты эквивалентны только, если поля <code>{@link #name}</code> и <code>{@link #mode}</code>
     * имеют одинаковое значение и объект является классом-наследником {@link Command}
     * @param obj сравниваемый объект
     * @return {@code true} если объекты эквивалентны; {@code false} если объекты различаются
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Command){
            if (getName().equals(((Command) obj).getName()) && getMode() == ((Command) obj).getMode()){
                return true;
            }
        }
        return false;
    }
}
