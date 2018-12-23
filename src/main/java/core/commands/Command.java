package core.commands;

import core.commands.enums.CommandEnum;
import core.commands.exceptions.CommandHasNoCommandEnumException;
import core.commands.exceptions.CommandHasNoNameException;

/**
 * Абстрактный класс для всех исполняемых команд
 * Поле {@link #name} идентифицирует команду, т.е команда вызывается по этому имени
 *
 * @author Артур Куприянов
 * @version 1.0
 */
public abstract class Command {
    protected String name = null;
    private static int counter = 0;
    private int hashCode;

    {
        hashCode = counter++;
        setName();
    }

    /**
     * Метод реализации команды
     *
     * @return Строку, возвращаемую пользователю
     */
    public abstract String init(String ... args);

    protected abstract void setName();

    public String init(String params){
        return init(params.split(" "));
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
     *
     * @return форматированное имя и мод команды
     */
    @Override
    public String toString() {
        return String.format("name: %s",this.getName());
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
