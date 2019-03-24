package core.modules.quest;

import com.google.gson.GsonBuilder;

import java.util.*;

/**
 * @author Arthur Kupriyanov
 */
public class SavedQuestions {
    private Map<String, Set<Integer>> data;
    SavedQuestions(Map<String, Set<Integer>> data){
        this.data = data;
    }
    public SavedQuestions(){
        data = new HashMap<>();
    }

    public static SavedQuestions getInstanceFromJSON(String json){
        return new GsonBuilder().create().fromJson(json, SavedQuestions.class);
    }

    public Set<Integer> getByTag(String tag){
        if (data.containsKey(tag)) return data.get(tag);
        return null;
    }

    public void saveQuest(String tag, int id){
        Set<Integer> ids;
        if (data.containsKey(tag)){
            ids = data.get(tag);
            ids.add(id);
            data.replace(tag, ids);
        } else{
            ids = new HashSet<>();
            ids.add(id);
            data.put(tag, ids);
        }
    }

    public void deleteQuest(String tag, int id){
        if (data.containsKey(tag)){
            Set<Integer> ids = data.get(tag);
            ids.remove(id);
            if (ids.isEmpty()) data.remove(tag);
            else data.replace(tag, ids);
        }
    }

    /**
     * Удалить все вхождения вопроса с указанным ID
     * @param id номер вопроса
     */
    public void deleteQuest(int id){
        for (String tag : data.keySet()){
            Set<Integer> value = data.get(tag);
            value.remove(id);
            if (value.isEmpty()) data.remove(tag);
            else data.replace(tag, value);
        }
    }

    public Map<String, Set<Integer>> getAll(){
        return data;
    }

    public String getStatistic(){
        StringBuilder sb = new StringBuilder();
        for (String tag : data.keySet()){
            sb.append(tag).append(": ").append(data.get(tag).size());
            sb.append(" вопрсов\n");
        }

        return sb.toString();
    }

    public String toJSON(){
        return new GsonBuilder().create().toJson(this);
    }
}
