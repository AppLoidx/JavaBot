package core.modules.queue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author Arthur Kupriyanov
 */
public class Request implements Serializable {
    HashMap<Integer, ArrayList<Integer>> requestList = new HashMap<>();

    public boolean isAccepted(int userID, int secondUserID){
        System.out.println(requestList.values().toString());
        if (requestList.containsKey(userID)){
            if (!requestList.get(userID).contains(secondUserID)){
                return false;
            }
        } else {
            return false;
        }

        if (requestList.containsKey(secondUserID)){
            if (!requestList.get(secondUserID).contains(userID)){
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public void deleteRequest(int userID, int secondUserID){
        System.out.println("Before delete: " + requestList.toString());
        if (requestList.containsKey(userID)){
            ArrayList<Integer> oldValue = requestList.get(userID);
            int key = 0;
            for (int value : oldValue) {
                if (value == secondUserID) {
                    break;
                }
                key++;
            }

            oldValue.remove(key);
            requestList.replace(userID, oldValue);
        }

        System.out.println("After delete: " + requestList.toString());
    }

    public void addSwapRequest(int userID, int secondUserID){
        if (requestList.containsKey(userID)){
            ArrayList<Integer> newValue = requestList.get(userID);
            if (!newValue.contains(secondUserID)) {
                newValue.add(secondUserID);
                requestList.replace(userID, newValue);
            }

        } else {
            ArrayList<Integer> newValue = new ArrayList<>();
            newValue.add(secondUserID);
            requestList.put(userID, newValue);
        }
    }

}
