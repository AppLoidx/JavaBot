package core.common;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JSONReader {
    public static JSONObject getJSONObject(String filename) throws ParseException, IOException {
        BufferedReader reader = new BufferedReader( new FileReader(filename));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while( ( line = reader.readLine() ) != null ) {
            stringBuilder.append( line );
        }
        JSONParser jsonParser = new JSONParser();

        return  (JSONObject) jsonParser.parse(stringBuilder.toString());

    }
}
