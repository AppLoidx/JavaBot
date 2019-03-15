package vk;

import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import core.Commander;

/**
 * @author Arthur Kupriyanov
 */
public class Responser {
    /**
     * Обращается к классу {@link Commander} сначала с аргументом типа {@link Message},
     * если такой команды не найдется (выполнится команда {@link core.commands.Unknown},
     * которая возвращает null - выполнится обычная команда с методом init(String) c
     * с дополнинением к сигнатуре метаданных
     *
     * @return В случае успешного выполнения - строку, в случае, если команда не
     * нуждается в автоматической отправке ответа ( либо он осуществляется внутри
     * самой программы)  - <code>""</code>. Исключительным случаем является возвращение
     * ответа от команды {@link core.commands.Unknown}, которая возвращает строковый ответ о том,
     * что команда не найдена.
     * @see Commander
     * @see core.CommandDeterminant
     * @see core.commands.Unknown
     */
    public static String getResponse(Message message){

        String vkResponse =  Commander.getResponse(message);
        if (vkResponse != null ){
            if (vkResponse.equals("")){
                return null;
            }
            return vkResponse;
        } else {
            String extra = "";
            extra += " --#user_id " + message.getUserId();
            UserXtrCounters info = VKManager.getUserInfo(message.getUserId());
            extra += " --#first_name " + info.getFirstName();
            extra += " --#last_name " + info.getLastName();

            return Commander.getResponse(message.getBody() + extra);
        }

    }
}
