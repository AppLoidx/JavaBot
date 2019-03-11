package core.commands;

import com.vk.api.sdk.objects.messages.Message;
import core.commands.VKCommands.VKCommand;
import core.modules.Date;
import core.modules.parser.ScheduleParser;

/**
 * @author Arthur Kupriyanov
 */
public class Test extends Command implements VKCommand {
    @Override
    protected void setConfig() {
        commandName = "test";
    }

    @Override
    public String exec(Message message) {
        String msg = "";
        msg += "Day: " + Date.getDayOfWeek();
        msg += "\n parity : " + ScheduleParser.getWeekParity();
        msg += "\n Day increament " + Date.increaseDayOfWeek(Date.getDayOfWeek());
        msg += "\n parity increment : " + ScheduleParser.getWeekParity(1);
        msg += "\n parity increment2 : " + ScheduleParser.getWeekParity(2);
        msg += "\n parity increment3 : " + ScheduleParser.getWeekParity(3);
        msg += "\n\n week" + Date.getWeekOfYear();

        return msg;
    }
}
