package core.commands;

import core.common.KeysReader;
import core.modules.Date;
import core.modules.parser.WeatherParser;

import java.io.IOException;
import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class Day extends Command {
    @Override
    public String init(String... args) {
        Map<Integer, Map<String, String>> keysMap = KeysReader.readOrderedKeys(args);

        StringBuilder stringBuilder = new StringBuilder();

        int day = 0;
        boolean isTemperatureKey = false;
        boolean isFullKey = false;
        boolean isDayKey = false;

        for (int key: keysMap.keySet()
        ) {
            if(keysMap.get(key).containsKey("-d")){
                try{
                    day = Date.increaseDayOfWeek(Integer.valueOf(keysMap.get(key).get("-d")));
                    isDayKey = true;
                } catch (NumberFormatException e){
                    return "Неверно введен параметр ключа -d";
                }
            }
            if (keysMap.get(key).containsKey("-t")){
                isTemperatureKey = true;
            }
            if (keysMap.get(key).containsKey("-f")){
                isFullKey = true;
            }
        }

        if (isTemperatureKey) {
            try {
                WeatherParser weatherParser = new WeatherParser();
                if (isFullKey && isDayKey && day==1){
                    stringBuilder.append(weatherParser.getWeatherTodayDescription());
                }

                if (isFullKey && !isDayKey) {
                    stringBuilder.append(weatherParser.getWeatherFullDescription());
                }

                if (!isFullKey){
                    stringBuilder.append(weatherParser.getTemperature(day-1));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return stringBuilder.toString();
    }

    @Override
    protected void setName() {
        name = "day";
    }
}
