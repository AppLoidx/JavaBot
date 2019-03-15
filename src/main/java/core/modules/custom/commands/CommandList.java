package core.modules.custom.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Arthur Kupriyanov
 */
public class CommandList {
    /** time , [command, id]*/
    private HashMap<String, HashMap<String, String>> list = new HashMap<>();
    private int lastId = 0;

    public void addCommand(String time, String command){
        HashMap<String, String> oldInnerList;
        if (list.containsKey(time)){
            oldInnerList = list.get(time);
        } else oldInnerList = new HashMap<>();
        oldInnerList.put(command, String.valueOf(lastId));
        list.put(time, oldInnerList);
        lastId++;
    }

    public ArrayList<String> getCommandByTime(String time){
        for(String t : list.keySet()){
            if (t.equals(time)){
                return new ArrayList<>(list.get(t).keySet());
            }
        }
        return null;
    }

    public void deleteByID(String id){
        String deleteableKey = null;
        HashMap<String, String> newValue = null;
        for(String t : list.keySet()){
            HashMap<String, String> innerMap = list.get(t);
            Iterator<String> iterator = innerMap.keySet().iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                if (innerMap.get(key).equals(id)){
                    innerMap.remove(key);
                    newValue = innerMap;
                    deleteableKey = t;
                    break;
                }
            }
        }
        if (deleteableKey != null){
            list.put(deleteableKey, newValue);
            if (list.get(deleteableKey).isEmpty()){
                list.remove(deleteableKey);
            }
        }
    }

    public HashMap<String, HashMap<String, String>> getList() {
        return list;
    }

}
