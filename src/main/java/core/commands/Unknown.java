package core.commands;

public class Unknown extends Command {

    @Override
    protected void setName() {
        commandName = "unknown";
    }
    @Override
    public String init(String... args) {
        return "Не распознанная команда";
    }
}
