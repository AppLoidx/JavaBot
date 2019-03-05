package core.common;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Класс для работы с чтением ключа при вводе команды
 *
 * @author Артур Куприянов
 * @version 1.1
 */
public class KeysReader {
    /**
     * Карта ключей с их значениями, если значение ключа не было указано, то значение - пустое
     * version: 1.2
     * @param words массив ключей и их значений, ключи должны начинаться с "-" или "--"
     * @return карту вида ключ - значение
     */
    public static Map<String, String> readKeys(String[] words){
        Map<String, String> keyMap = new HashMap<>();

        // резервируем ключ для его значения
        String reservedKey = null;

        StringBuilder value = new StringBuilder();

        boolean firstWord = true;
        boolean isOneWord = true;

        for (String word: words
             ) {

            if (word.matches("-[a-z]") || word.matches("--[a-z#_]*")) {
                // Если ключ пустой
                if (reservedKey != null){
                    if(isOneWord) keyMap.put(reservedKey, value.toString());
                    else{
                        keyMap.put(reservedKey, value.toString().trim());

                    }

                    isOneWord = true;
                    firstWord = true;
                    value = new StringBuilder();
                }

                reservedKey = word;

            } else if (reservedKey != null) {
                if (!firstWord){
                    value.append(" ");
                    isOneWord = false;
                }
                else firstWord = false;

                value.append(word);

                //value.append(word);

                //keyMap.put(reservedKey, value.toString());//word);
                //value = new StringBuilder();
                //reservedKey = null;
            }

            if (word.matches("-[a-z]*")){
                String[] keys = word.split("");
                for (String key: keys
                     ) {
                    if(key.matches("[a-z]")) {
                        keyMap.put("-"+key, "");
                    }
                }
            }
        }

        if (reservedKey != null){ keyMap.put(reservedKey, value.toString().trim()); }

        if (keyMap.isEmpty()){
            keyMap.put("error","empty");
        }
        return keyMap;
    }

    /**
     * Перегрузка {@link #readKeys(String[])}
     * @param words строковый запрос
     * @return карту вида ключ - значение
     */
    public static Map<String, String> readKeys(String words){
        return readKeys(words.split(" "));
    }

    /**
     * Сортированная карта ключей и их значений
     * version: 1.0
     * @param words массив ключей и их значений, ключи должны начинаться с "-" или "--"
     * @return крату вида [index, [ключ, значение]]
     */
    public static TreeMap<Integer, Map<String, String>> readOrderedKeys(String[] words){
        TreeMap<Integer, Map<String, String>> response = new TreeMap<>();

        // резервируем ключ для его значения
        String reservedKey = null;

        int counter = 0;

        for (String word: words
        ) {

            if (word.matches("-[a-z]") || word.matches("--[a-z]*")) {
                // Если ключ пустой
                if (reservedKey != null){
                    Map<String, String> keyMap = new HashMap<>();
                    keyMap.put(reservedKey, "");
                    response.put(counter, keyMap);
                    counter++;

                }

                reservedKey = word;
            } else if (reservedKey != null){
                Map<String, String> keyMap = new HashMap<>();
                keyMap.put(reservedKey, word.trim());
                reservedKey = null;

                response.put(counter, keyMap);
                counter++;

            }else if (word.matches("-[a-z]*")) {
                String[] keys = word.split("");
                for (String key : keys
                ) {
                    if (key.matches("[a-z]")) {
                        Map<String, String> keyMap = new HashMap<>();
                        keyMap.put("-" + key, "");
                        response.put(counter, keyMap);
                        counter++;
                    }
                }
            }



        }

        if (reservedKey != null) {
            Map<String, String> temp = new HashMap<>();
            temp.put(reservedKey, "");
            if (response.isEmpty()) {
                response.put(1, temp);
            } else {
                response.put(response.lastKey() + 1, temp);
            }
        }
        return response;
    }

}
