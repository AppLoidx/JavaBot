package core;

import java.util.Scanner;

public class CommanderTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Test started!");
        while (true) {
            System.out.println(Commander.getResponse(sc.nextLine()));
        }
        }


}
