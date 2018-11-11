package core.commands;

public class Unknown extends Command {
    @Override
    public String init(String... args) {
        return "Не распознанная команда";
    }
}
