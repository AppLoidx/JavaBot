package core.modules.queue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

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
}
