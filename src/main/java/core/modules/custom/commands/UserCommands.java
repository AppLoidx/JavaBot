package core.modules.custom.commands;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Arthur Kupriyanov
 */
public class UserCommands {
    private HashMap<String, ArrayList<UserCmd>> timedList = new HashMap<>();
    private int counter = 0;

    public void addCommand(String time, String command){
        counter++;
        ArrayList<UserCmd> oldArray;
        if (timedList.containsKey(time)){
            oldArray = timedList.get(time);
        } else oldArray = new ArrayList<>();
        oldArray.add(new UserCmd(counter, command));
        timedList.put(time, oldArray);
    }

    /**
     * Используется для добавления команды программно, не вызывает увеличения серийного номера
     * @param time время срабатывания команды
     * @param cmd выполняемая команда с ID
     */
    public void addCommand(String time, UserCmd cmd){
        ArrayList<UserCmd> oldArray;
        if (timedList.containsKey(time)){
            oldArray = timedList.get(time);
        } else oldArray = new ArrayList<>();
        oldArray.add(cmd);
        timedList.put(time, oldArray);
    }

    /**
     * Используется для добавления команды программно, не вызывает увеличения серийного номера
     * @param time время срабатывания команды
     * @param cmdList список выполняемых команд с ID
     */
    public void addCommand(String time, ArrayList<UserCmd> cmdList){
        ArrayList<UserCmd> oldArray;
        if (timedList.containsKey(time)){
            oldArray = timedList.get(time);
        } else oldArray = new ArrayList<>();
        oldArray.addAll(cmdList);
        timedList.put(time, oldArray);
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void setTimedList(HashMap<String, ArrayList<UserCmd>> timedList) {
        this.timedList = timedList;
    }
}
