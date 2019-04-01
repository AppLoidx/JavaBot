package core.modules;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class Aliases {
    @SerializedName("alias_map")
    private Map<String, String> aliasMap = new HashMap<>();

    public void add(String key, String value){
        aliasMap.put(key.trim(), value.trim());
    }

    public boolean contains(String key){
        return aliasMap.containsKey(key);
    }

    public void delete(String key){
        aliasMap.remove(key);
    }

    public String get(String key){
        if (aliasMap.containsKey(key)){
            return aliasMap.get(key);
        }
        return null;
    }

    public String toJSON(){
        return new GsonBuilder().create().toJson(this);
    }

    public Map<String, String> getAll(){
        return aliasMap;
    }
    public boolean isEmpty(){
        return aliasMap.isEmpty();
    }
}
