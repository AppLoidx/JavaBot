package core.commands;

import core.common.KeysReader;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class Queue extends Command {


    @Override
    void setName() {
        name = "queue";
    }

    private String basePathGroups = "src/main/botResources/database/groups";
    private String basePathQueues = "src/main/botResources/database/queues";

    @Override
    public String init(String... args) {
        Map<String, String> keysMap = KeysReader.readKeys(args);

        String group = "P3112"; //TODO: REWRITE FROM DATABASE
        int id = 0;

        core.modules.queue.Queue queue = new core.modules.queue.Queue();
//        core.modules.queue.Queue.QueueEdit queueEdit = new core.modules.queue.Queue.QueueEdit(this.basePathQueues+"/"+group);

        // TODO: Exceptions with string -> int
        if (keysMap.containsKey("-i") || keysMap.containsKey("--id")){
            if (keysMap.containsKey("-i")){
                id = Integer.valueOf(keysMap.get("-i"));
            } else {
                id = Integer.valueOf(keysMap.get("--id"));
            }
        }

        if (keysMap.containsKey("-c") || keysMap.containsKey("--create")){
            if (id == 0){
                try {
                    core.modules.queue.Queue.createQueue(basePathQueues,group,basePathGroups+ "/"+group+".json");
                    return "Очередь успешно создана!";
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                    return "Ошибка при создании очереди";
                }

            }
            try {
                core.modules.queue.Queue.createQueue(basePathQueues,group,basePathGroups+ "/"+group+".json",id);
                return "Очередь создана!";
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                return "Ошибка при создании очереди";
            }
        }

        if (keysMap.containsKey("-n") || keysMap.containsKey("--next")){
            try {
                core.modules.queue.Queue.QueueEdit
                        queueEdit = new core.modules.queue.Queue.QueueEdit(basePathQueues+"/"+group);
                queueEdit.nextStage();
                return "ok!";

            } catch (IOException e) {
                e.printStackTrace();
                return "Ошибка редактировании очереди";
            }
        }

        if (keysMap.containsKey("-s") || keysMap.containsKey("--set")){
            if (id == 0){
                return "Укажите параметр -i или --id, чтобы указать ID";
            }

            core.modules.queue.Queue.QueueEdit queueEdit;
            try {
                queueEdit = new core.modules.queue.Queue.QueueEdit(basePathQueues+"/"+group);
                queueEdit.setNextStatusById(id);
                return "ok!";

            } catch (IOException e) {
                e.printStackTrace();
                return "Ошибка в параметре -s";
            }
        }

        if (keysMap.containsKey("-g") || keysMap.containsKey("--get")){
            StringBuilder response = new StringBuilder();
            try {
                TreeMap<Integer, core.modules.queue.Queue.Person> queueTreeMap = core.modules.queue.Queue.readQueue(basePathQueues + "/" + group);
                for (int key: queueTreeMap.keySet()
                     ) {
                    core.modules.queue.Queue.Person person = queueTreeMap.get(key);
                    response.append(key).append(" ").append(person.getName()).append(" ID: ").append(person.getId());
                    response.append(" ");
                    response.append(person.getStatus());
                    response.append("\n");
                }

                return response.toString();
            }catch (IOException e) {
                e.printStackTrace();
                return "Ошибка в параметре  -g";
            }
        }

        if (keysMap.isEmpty()){
            return "Пожайлуйста введите параметры";
        }else{
            return "Убедитесь в правильности введенных параметров";
        }
    }
}
