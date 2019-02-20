package core.modules.queue;

import java.io.*;

/**
 * @author Arthur Kupriyanov
 */
public class QueueLoader<T> {
    private static String path = "queue/";

    public T loadQueue(String name) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path + name));
        Object queue = ois.readObject();
        ois.close();
        return  (T) queue;

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
