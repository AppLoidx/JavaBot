package core.modules.tracer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * @author Arthur Kupriyanov
 */
public class StringBcompProgrammReader {
    public static TreeMap<String, String> read(String table){
        TreeMap<String, String> treeMap = new TreeMap<>();

        for (String line: table.split("\n")){
            StringTokenizer tokenizer = new StringTokenizer(line, "|");
            while(tokenizer.hasMoreTokens()){
                String value = tokenizer.nextToken();
                if (value.matches(".*:.*")) {
                    treeMap.put(value.split(":")[0].trim(), value.split(":")[1].trim());
                }
            }
        }

        return treeMap;
    }

}
