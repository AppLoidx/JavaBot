package core.modules.custom.commands;

/**
 * @author Arthur Kupriyanov
 */
public class UserCmd {
    public final String command;
    public final int id;
    public UserCmd(int id, String cmd){
        this.command = cmd;
        this.id = id;
    }
}
