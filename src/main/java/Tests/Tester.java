package Tests;


import core.modules.Time;
import core.modules.parser.AuditoryParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tester {

    public static void main(String[] args) throws IOException, ParseException {

        Map<String, String> map = new HashMap<>();
        String value = "1";
        ArrayList al = new ArrayList();
        map.put("key", value);
        al.add(map);
        System.out.println(al.toString());
        value = "2";
        System.out.println(al.toString());
        map.put("key2", "calue2");
        System.out.println(al.toString());

    }
}
