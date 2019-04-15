package vk;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arthur Kupriyanov
 */
public class HandlerManager {
    private static List<Thread> threads = new ArrayList<>();
    public static void addHandler(Runnable runnable){
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        threads.add(t);
    }
    public static void runThreads(){
        for (Thread t : threads){
            t.start();
        }
    }
}
