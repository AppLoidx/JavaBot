package Tests;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Tester {
    public static void main(String[] args) throws IOException, ParseException {
        BufferedReader reader = new BufferedReader( new FileReader("C:\\java\\Bot\\src\\main\\java\\core\\commands\\resources\\links.json"));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while( ( line = reader.readLine() ) != null ) {
            stringBuilder.append( line );
        }
        JSONParser jsonParser = new JSONParser();

        JSONObject jsonObject = (JSONObject) jsonParser.parse(stringBuilder.toString());
        System.out.println(jsonObject.get("opd"));

    }
}
