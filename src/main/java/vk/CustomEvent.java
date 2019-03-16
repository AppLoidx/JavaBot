package vk;

import core.modules.Date;
import core.modules.custom.commands.CommandList;
import core.modules.custom.commands.CustomCommandDB;
import core.modules.vkSDK.MessageConverter;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;

/**
 * @author Arthur Kupriyanov
 */
public class CustomEvent implements Runnable {
    private static HashMap<String, Integer> timeLockList = new HashMap<>();
    private CustomCommandDB customCommandDB = new CustomCommandDB();
    @Override
    public void run() {
        while (true) {
            try {
                    HashMap<Integer, CommandList> list = customCommandDB.getAll();
                    for (int vkid : list.keySet()) {
                        CommandList cl = list.get(vkid);
                        for (String time : cl.getList().keySet()) {
                            if (Date.getTimeNow().equals(time) && !isTimeLocked(Date.getTimeNow())) {
                                MessageConverter mc = new MessageConverter();
                                mc.setUserId(vkid);
                                for (String cmd : cl.getList().get(time).keySet()) {
                                    mc.setBody(cmd);
                                    new VKManager().sendMessage(Responser.getResponse(mc.buildMessage()), 255396611);
                                    lockTime(time);
                                }
                            }
                        }
                    Thread.sleep(20000);
                }
            } catch (InterruptedException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isTimeLocked(String time){
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
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
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        timeLockList.put(time, dayOfWeek);
    }
}

