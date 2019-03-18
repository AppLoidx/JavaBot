package core.commands;

/**
 * Интерфейс для служебных команд
 * @author Arthur Kupriyanov
 */
@FunctionalInterface
public interface ServiceCommand{

    /** Основной вызываемый метод*/
    void service();
}
