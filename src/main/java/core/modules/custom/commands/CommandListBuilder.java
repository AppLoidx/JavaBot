package core.modules.custom.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Arthur Kupriyanov
 */
public class CommandListBuilder {
    private static final Gson gson = new GsonBuilder().create();
    public static CommandList build(String json){
        return gson.fromJson(json, CommandList.class);
    }

    public static String convert(CommandList cl){
        return gson.toJson(cl);
    }
}
