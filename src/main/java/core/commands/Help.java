package core.commands;

import com.vk.api.sdk.objects.messages.Message;
import core.CommandManager;
import core.commands.VKCommands.VKCommand;

import java.util.ArrayList;


/**
 * @author Arthur Kupriyanov
 */
public class Help extends Command implements VKCommand, Helpable {
    @Override
    protected void setConfig() {
        commandName = "help";
    }

    @Override
    public String exec(Message message) {
        ArrayList<String> garbage = new ArrayList<>();
        String[] args = message.getBody().split(" ");
        if (args.length > 1){
            StringBuilder sb = new StringBuilder();
            for (int i=1; i< args.length ; i++){
                boolean found = false;
                boolean repeated = false;
               for (Command command : CommandManager.getCommands()){
                   if (command.getName().equals(args[i])){
                       if (garbage.contains(command.getName())){
                           found = true;
                           repeated = true;
                           continue;
                       } else garbage.add(command.getName());
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
               if (!repeated) {
                   sb.append("\n------------\n");
               }
            }
            return sb.toString();
        } else {
            StringBuilder helpableCommands = new StringBuilder();
            StringBuilder otherCommands = new StringBuilder();
            helpableCommands.append("Список команд поддерживающих документацию:\n");
            otherCommands.append("Команды без документации:\n");
            for(Command command : CommandManager.getCommands()){

                if (command instanceof Helpable){
                    helpableCommands.append(command.getName())
                            .append(" -- ").append(((Helpable) command).getDescription())
                            .append("\n");
                } else otherCommands.append(command.getName()).append("\n");
            }

            helpableCommands.append("-------------\n").append(otherCommands.toString());
            helpableCommands.append("\n\n").append("Также доступна команда help help");

            return helpableCommands.toString();
        }

    }

    @Override
    public String getManual() {
        return
        "При использовании просто команды \"help\"\n" +
                "Выводит список доступных команд.\n\n" +
                "В разделе команд, есть те, которые имеют расширенную документацию. Для " +
                "вызова более расширенного описания - введите help [имя_команды]\n\n" +
                "Если хотите получить документацию сразу по нескольким командам - используйте " +
                "список команд, разделенных пробелом:\n" +
                "help [имя_команды_1] [имя_команды_2]";
    }

    @Override
    public String getDescription() {
        return "Команда для вывода мануала по остальным командам";
    }
}
