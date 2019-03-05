package core.commands;

import core.common.KeysReader;
import core.common.UserInfoReader;
import core.modules.UsersDB;
import core.modules.queue.Person;
import core.modules.queue.SimpleQueue;
import core.modules.queue.Stat;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class QueueSimpleHandler{
    public <T extends SimpleQueue> String handle(T queue, String... args){
        Map<String, String> keysMap = KeysReader.readKeys(args);

        String userID = UserInfoReader.readUserID(args);

        if (userID == null){
            return "Ошибка при определении пользователя";
        }
        boolean writeAccess = false;
        boolean readAccess = false;
        boolean executeAccess = false;

        if (queue.getWriteAccessList().isEmpty() || queue.getWriteAccessList().contains(userID)){
            writeAccess = true;
        }
        if (queue.getReadAccessList().isEmpty() || queue.getReadAccessList().contains(userID)){
            readAccess = true;
        }
        if (queue.getExecuteAccessList().contains(userID)){
            executeAccess = true;
        }

        String optionallyMsg;

        if (keysMap.containsKey("-a")){
            if (queue.getUsers().contains(userID)){
                return "Вы уже в очереди";
            }
            System.out.println(userID);
            String userFullname = null;
            try {
                UsersDB usersDB = new UsersDB();
                if (!usersDB.checkUserExsist(Integer.valueOf(userID))){
                    return "Вас нет в базе данных, пожалуйста зарегестрируйтесь командой reg";
                }
                userFullname = usersDB.getFullNameByVKID(Integer.valueOf(userID)) ;
            } catch (SQLException e) {
                e.printStackTrace();

            }
            if (userFullname == null){
                return "Ошибка при работе с базой данных";
            }
            queue.addPerson(new Person(userFullname).setVKID(userID));
            queue.addUserID(userID);

            try {
                queue.saveQueue();
                return "Вы успешно добавлены в очередь";
            } catch (IOException e) {

                return "Ошибка при сохранении очереди";
            }
        }

        // Вывод очереди
        if (keysMap.containsKey("-r")){
            return queue.getFormattedQueue();
        }

        // Описание очереди
        if (keysMap.containsKey("-i") && !queue.getExecuteAccessList().contains(userID)){
            return queue.getDescription();
        }
        if (keysMap.containsKey("-i") && queue.getExecuteAccessList().contains(userID)){
            queue.setDescription(keysMap.get("-i"));
        }

        if (keysMap.containsKey("-g")){
            Stat stat = queue.getStat();

            int peopleCount = stat.peopleCount;
            int passCount = stat.passCount;

            return "Человек в очереди: " + peopleCount +
                    "\nЧеловек прошло: " + passCount;
        }

        if(keysMap.containsKey("-s")){
            int secondUserID;
            try{
                secondUserID = Integer.valueOf(keysMap.get("-s"));
                int userQueueID = queue.getIDByVKID(userID);

                queue.request.addSwapRequest(userQueueID, secondUserID);

                if (queue.request.isAccepted(userQueueID, secondUserID)){

                    queue.swap(userQueueID, secondUserID);
                    queue.request.deleteRequest(userQueueID, secondUserID);
                    queue.request.deleteRequest(secondUserID, userQueueID);
                    try {
                        queue.saveQueue();
                    } catch (IOException e) {

                        return "Ошибка при сохранении очереди";
                    }
                    return "Вы поменялись с ID: " + secondUserID;
                }

                try {
                    queue.saveQueue();
                } catch (IOException e) {

                    return "Ошибка при сохранении очереди";
                }
                return "Заявка на обмен местами отправлен пользователю с ID: " + secondUserID;
            } catch (NumberFormatException e){

                return "Неверный формат ключа -s [int_ИД_ПОЛЬЗОВАТЕЛЯ]";
            }
        }
        return null;
    }


}
