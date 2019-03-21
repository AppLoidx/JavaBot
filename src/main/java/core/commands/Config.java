package core.commands;

import com.vk.api.sdk.objects.messages.Message;
import core.commands.VKCommands.VKCommand;
import core.modules.config.Configuration;
import core.modules.config.Settings;

/**
 * @author Arthur Kupriyanov
 */
public class Config extends Command implements VKCommand, Helpable {

    @Override
    protected void setConfig() {
        commandName = "config";
    }

    @Override
    public String exec(Message message) {
        String[] msg = message.getBody().split(" ");
        Settings settings = Configuration.getSettings(message.getUserId());
        StringBuilder sb = new StringBuilder();
        for(String word : msg){
            if (word.equals("show")){
                String es = settings.isEveningSpam() ? "да":"нет";
                String ms = settings.isMorningSpam() ? "да":"нет";
                String appAccess = settings.isAppAccess() ? "да":"нет";
                String img = settings.isImages() ? "да":"нет";

                sb.append("Утренний спам [ms]: ").append(ms).append("\n");
                sb.append("Вечерний спам [es]: ").append(es).append("\n");
                sb.append("Доступ приложениям [app]: ").append(appAccess).append("\n");
                sb.append("Картинки [img]: ").append(img).append("\n");

                return sb.toString();
            }
            if (word.matches(".*=.*")){
                String[] temp = word.split("=");
                String key = temp[0];
                String val = temp[1];
                if (!val.equals("1") && !val.equals("0")){
                    sb.append("Значение ").append(val).append(" для ключа ").append(key);
                    sb.append(" установлена неправильно!").append("\n");
                    continue;
                }
                boolean value = val.equals("1");

                switch (key){
                    case "ms":
                        settings.setMorningSpam(value);
                        break;
                    case "es":
                        settings.setEveningSpam(value);
                        break;
                    case "app":
                        settings.setAppAccess(value);
                        break;
                    case "img":
                        settings.setImages(value);
                        break;
                        default:
                            sb.append("Значение настройки ").append(key).append(" не найдено!");
                            sb.append("\n");
                            continue;
                }
                sb.append("Настройка ").append(key).append(" установилась на значение ").append(value);
                sb.append("\n");
            }
        }

        if (sb.toString().equals("")){
            return "Вы не ввели ни одного значения с ключом";
        }
        boolean successful = settings.setVkid(message.getUserId()).save();
        if (successful){
            return sb.toString();
        }else return "Ошибка. Настройки не сохранились!";
    }

    @Override
    public String getManual() {
        return "Команда для настройки персональных конфигураций\n" +
                "Чтобы посмореть свои конфигурации, введите:\n" +
                "config show\n" +
                "\nДля того, чтобы указать значение:\n" +
                "config имя_параметра=значение\n" +
                "Поля, в которых указываются значения да/нет, ставьте " +
                "1 (true) или 0(false)\n" +
                "\nНапример:\n" +
                "config ms=1 или config app=0\n" +
                "Короткие имена параметров можно увидеть вместе с параметрами в квадратных скобках!\n" +
                "Вики: https://github.com/AppLoidx/JavaBot/wiki/команда-Config";
    }

    @Override
    public String getDescription() {
        return "Настройка конфигураций";

    }
}
