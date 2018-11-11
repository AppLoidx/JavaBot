package core.commands;

import java.util.HashMap;
import java.util.Map;

class KeysReader {
    static Map<String, String> readKeys(String[] words){
        Map<String, String> keyMap = new HashMap<>();

        // резервируем ключ для его значения
        String reservedKey = null;

        for (String word: words
             ) {
            if (word.matches("-[a-z]") || word.matches("--[a-z]*")) {
                // Если ключ пустой
                if (reservedKey != null){ keyMap.put(reservedKey, ""); }

                reservedKey = word;
            } else if (reservedKey != null){
                keyMap.put(reservedKey, word);
                reservedKey = null;
            }
        }

        if (reservedKey != null){ keyMap.put(reservedKey, ""); }

        if (keyMap.isEmpty()){
            keyMap.put("error","empty");
        }
        return keyMap;
    }
}
