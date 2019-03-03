package core.commands;

import core.common.KeysReader;
import core.common.UserInfoReader;
import core.modules.queue.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Arthur Kupriyanov
 */
public class Queue extends Command implements ProgramSpecification{

    @Override
    protected void setConfig() {
        this.commandName = "queue";
    }

    @Override
    public String programInit(String... args) {
        return init(args);
    }

    private enum QueueType{
        SIMPLE,
        SHUFFLE,
        CONSISTENT,
        DATED,
        FIXED,
        UNKNOWN
        }

    @Override
    public String init(String... args) {
        Map<String, String> keysMap = KeysReader.readKeys(args);
        String name;
        boolean isProgram = UserInfoReader.checkIsProgramm(args);

        // a - add, d - delete, s - swap , l - length , p - person,
        // t - time, e - secondtime, r - reveal, с - create, k - kill,
        // n - name, i - digit, f - shuffle,
        if(keysMap.containsKey("-n") || keysMap.containsKey("--name")){
            name = keysMap.get("-n");
        } else {
            if (isProgram){
                return "400";
            }
            return "Не указан обязательный ключ -n [имя_очереди]";
        }

        // работа с ключом создания очереди
        if (keysMap.containsKey("-c") || keysMap.containsKey("--create")){
            if (QueueLoader.checkExist(name)){
                if (isProgram){
                    return "409";
                }
                return "Очередь с таким именем уже существует";
            } else {
                core.modules.queue.Queue queue;
                String optionallyMsg;
                switch (getType(args)) {
                    case SHUFFLE:
                        queue = new ShuffleQueue(name);
                        optionallyMsg =  "Создана очередь " + name + " [shuffle]";
                        break;
                    case DATED:
                        if (keysMap.containsKey("-t")){
                            try {
                                queue = new DatedQueue(name, Integer.getInteger(keysMap.get("-t")));
                                optionallyMsg = "Создана очередь " + name + " [dated]";
                            } catch (NumberFormatException e){
                                if (isProgram){
                                    return "400";
                                }
                                return "Неверный формат параметра -t [время ]";
                            }
                        } else {
                            if (isProgram){
                                return "400";
                            }
                            return "Введите параметр -t [время окончания очереди]";
                        }
                        break;
                    case FIXED:
                        queue = new FixedQueue(name);

                        if (keysMap.containsKey("-l")){
                            try{
                                ((FixedQueue) queue).setLength(Integer.valueOf(keysMap.get("-l")));
                                optionallyMsg = "Создана очередь " + name + " [fixed]";
                            } catch (NumberFormatException e){
                                if (isProgram){
                                    return "400";
                                }
                                return "Неверный формат ключа -l [длина очереди]";
                            }
                        } else {
                            if (isProgram){
                                return "400";
                            }
                            return "Задайте параметр -l [длина очереди]";
                        }
                        break;
                    case CONSISTENT:
                        if (keysMap.containsKey("-t")){
                            try{
                                queue = new ConsistentQueue(name, Integer.valueOf(keysMap.get("-t")));
                                optionallyMsg = "Создана очередь " + name + " [consistent]";
                            } catch (NumberFormatException e){
                                if (isProgram){
                                    return "400";
                                }
                                return "Неверный формат параметра -t [время ]";
                            }
                        } else {
                            if (isProgram){
                                return "400";
                            }
                            return "Введите параметр -t [время окончания очереди]";
                        }
                        break;
                    default:
                        queue = new SimpleQueue(name);
                        optionallyMsg = "Создана очередь " + name + "[simple]";
                }
                try {
                    ((SimpleQueue) queue).addToExecuteAccessList(UserInfoReader.readUserID(args));
                    queue.saveQueue();

                    if (isProgram){
                        return "200";
                    }
                    return optionallyMsg;
                } catch (IOException e) {
                    e.printStackTrace();
                    if (isProgram){
                        return "500";
                    }
                    return "Ошибка при сохранении очереди " + e.getMessage();
                }
            }
        }

        // дальнейшая работа подразумевает уже созданную очередь и редактирование или изменение, удаление

        if (!QueueLoader.checkExist(name)){
            if (isProgram){
                return "404";
            }
            return "Очередь с именем " + name + " не существует";
        } else {
            try {
                core.modules.queue.Queue q = new QueueLoader<core.modules.queue.Queue>().loadQueue(name);
                QueueType queueType = getType(q);
                switch (queueType){
                    case SIMPLE:
                        SimpleQueue simpleQueue = (SimpleQueue) q;
                        return new QueueSimpleHandler().handle(simpleQueue, args);
                    case CONSISTENT:
                        ConsistentQueue consistentQueue = (ConsistentQueue) q;
                        return new QueueConsistentHandler().handle(consistentQueue, args);
                    case FIXED:
                        FixedQueue fixedQueue = (FixedQueue) q;
                        return new QueueFixedHandler().handle(fixedQueue, args);
                    case DATED:
                        DatedQueue datedQueue = (DatedQueue) q;
                        return new QueueDatedHandler().handle(datedQueue, args);
                    case SHUFFLE:
                        ShuffleQueue shuffleQueue = ((ShuffleQueue) q);
                        return new QueueShuffleHandler().handle(shuffleQueue, args);
                    case UNKNOWN:
                        if (isProgram){
                            return "400";
                        }
                        return "Ошибка при определении типа очереди";
                }


            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                if (isProgram){
                    return "500";
                }
                return "Ошибка при сериализации " + e.getMessage();
            }
        }
        return null;
    }

    private QueueType getType(core.modules.queue.Queue queue){
        if (queue.getClass() == SimpleQueue.class){
            return QueueType.SIMPLE;
        } else if (queue.getClass() == ConsistentQueue.class){
            return QueueType.CONSISTENT;
        } else if (queue.getClass() == DatedQueue.class){
            return QueueType.DATED;
        } else if (queue.getClass() == FixedQueue.class){
            return QueueType.FIXED;
        } else if (queue.getClass() == ShuffleQueue.class){
            return QueueType.SHUFFLE;
        } else {
            return QueueType.UNKNOWN;
        }
    }
    private QueueType getType(String ... args){
        TreeMap<Integer, Map<String, String>> keysMap = KeysReader.readOrderedKeys(args);
        for (Map<String, String> map: keysMap.values()
        ) {
            if (map.containsKey("-h") || map.containsKey("--shuffle")){ // shuffle
                return QueueType.SHUFFLE;
            }

            if (map.containsKey("-x") || map.containsKey("--fixed")){   // fixed
                return QueueType.FIXED;
            }

            if (map.containsKey("-q") || map.containsKey("--consistent")){ //sequence
                return QueueType.CONSISTENT;
            }

            if (map.containsKey("-b") || map.containsKey("--dated")){ // bound
                return QueueType.DATED;
            }

        }

        return QueueType.SIMPLE;
    }
}
