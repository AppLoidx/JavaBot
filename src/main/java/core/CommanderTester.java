package core;

import java.util.Scanner;

public class CommanderTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Test started!");
        String res;
        while (true) {
            res = sc.nextLine();
            new Thread(new Messanger(res)).start();

        }
        }

}
class Messanger implements Runnable{
    String message;
    public Messanger(String msg){
        message = msg;
    }
    public void sendMessage(String msg){
        System.out.println(msg);
    }

    @Override
    public void run() {
        sendMessage(Commander.getResponse(message));
    }
}