package core.commands;

public class Unknown extends Command {

    @Override
    void setName() {
        name = "unknown";
    }
    @Override
    public String init(String... args) {
        return "Не распознанная команда";
    }
}
