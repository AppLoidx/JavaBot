package core.modules.queue;

import java.io.*;

/**
 * @author Arthur Kupriyanov
 */
public class QueueLoader<T> {
    private static String path = "src/main/botResources/queue/";

    public T loadQueue(String name) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path + name));
        return  (T) ois.readObject();

    }

    public static boolean checkExist(String name){
        File file = new File(path + name);
        return file.exists();
    }

    public static void deleteQueue(String name) throws FileNotFoundException {
        File file = new File(path + name);
        if (!file.delete()){
            throw new FileNotFoundException("Файл не найден");
        }
    }
}
