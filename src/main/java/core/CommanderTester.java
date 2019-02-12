package core;

import java.util.Scanner;

public class CommanderTester {
    static int peer_id = 1;
    static {
        peer_id++;
    }

    public static void main(String... args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Test started!");
        String res;
        System.out.println("ID: " + peer_id);
        while (true) {
            res = sc.nextLine();
            new Thread(new Messanger(res, peer_id)).start();

        }

        }

    static class Messanger implements Runnable{
        String message;
        int peerId;
        public Messanger(String msg, int peerId)
        {
            message = msg;
            this.peerId = peerId;
        }
        public void sendMessage(String msg){
            System.out.println(msg);
        }

        @Override
        public void run() {
            String extra = "";
            extra += " --#user_id " + "1";
            extra += " --#first_name " + "user1";
            extra += " --#last_name " + "user1.1";
            sendMessage(Commander.getResponse(message +extra));
        }
    }
}
