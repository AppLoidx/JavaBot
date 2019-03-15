package core.modules.custom.commands;

import com.google.gson.Gson;

/**
 * @author Arthur Kupriyanov
 */
public class CommandListBuilder {
    private static final Gson gson = new Gson().newBuilder().create();
    public static CommandList build(String json){
        return gson.fromJson(json, CommandList.class);
    }

    public static String convert(CommandList cl){
        return gson.toJson(cl);
    }
}
