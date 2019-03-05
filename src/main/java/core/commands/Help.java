package core.commands;

import core.CommandManager;

/**
 * @author Arthur Kupriyanov
 */
public class Help extends Command {
    @Override
    protected void setConfig() {
        commandName = "help";
    }

    @Override
    public String init(String ... args) {

        if (args.length > 1){
            StringBuilder sb = new StringBuilder();
            for (int i=1; i< args.length ; i++){
                boolean found = false;
               for (Command command : CommandManager.getCommands()){
                   if (command.getName().equals(args[i])){
                       if (command instanceof Helpable){
                           sb.append(args[i])
                                   .append(":\n")
                                   .append(((Helpable) command).getManual());
                       } else {
                           sb.append("Команда ")
                                   .append(args[i])
                                   .append(" не поддерживает расширенной документации");
                       }
                       found = true;
                   }
               }
               if (!found){
                   sb.append("Команда ").append(args[i]).append(" не найдена");
               }
                sb.append("\n------------\n");
            }
            return sb.toString();
        } else {
            StringBuilder helpableCommands = new StringBuilder();
            StringBuilder allCommands = new StringBuilder();
            helpableCommands.append("Список команд поддерживающих документацию:\n");
            allCommands.append("Список всех команд:\n");
            for(Command command : CommandManager.getCommands()){
                allCommands.append(command.getName()).append("\n");
                if (command instanceof Helpable){
                    helpableCommands.append(command.getName())
                            .append(" -- ").append(((Helpable) command).getDescription())
                            .append("\n");
                }
            }

            helpableCommands.append("-------------\n").append(allCommands.toString());
            return helpableCommands.toString();
        }

    }

    public static void main(String[] args) {
        Help h = new Help();
        System.out.println(h.init("help"));
    }
}
