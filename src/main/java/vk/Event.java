package vk;

import core.commands.Command;
import core.commands.ServiceCommand;
import core.modules.Date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * @author Arthur Kupriyanov
 */
public class Event {

    private int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    private static HashMap<String, ArrayList<ServiceCommand>> timedCommands = new HashMap<>();
    private static HashMap<String, Integer> timeLockList = new HashMap<>();

    public void handlePerDay(){

        for(String time : timedCommands.keySet()) {
            if (!isTimeLocked(time)) {
                if (Date.getTimeNow().equals(time)) {
                    for (ServiceCommand cmd : timedCommands.get(time)) {
                        cmd.service();
                    }
                    lockTime(time);
                }
            }
        }
    }

    public void addCommand(String time, ServiceCommand cmd){
        ArrayList<ServiceCommand> serviceCommands;
        if (timedCommands.containsKey(time)){
            serviceCommands = timedCommands.get(time);
        } else{
            serviceCommands = new ArrayList<>();
        }
        serviceCommands.add(cmd);
        timedCommands.put(time, serviceCommands);
    }

    private boolean isTimeLocked(String time){
        if (timeLockList.containsKey(time)) {
            if (timeLockList.get(time) == dayOfWeek) {
                return true;
            } else {
                timeLockList.remove(time);
                return false;
            }
        } else {
            return false;
        }
    }

    private void lockTime(String time) {
        timeLockList.put(time, dayOfWeek);
    }

}
