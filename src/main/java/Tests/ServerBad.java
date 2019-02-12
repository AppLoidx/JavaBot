package Tests;


import core.modules.Time;
import core.modules.parser.AuditoryParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerBad {
        public static void main(String[] args) throws IOException {
            System.out.println("Welcome to ServerBad side");
            BufferedReader in = null;
            PrintWriter    out= null;

            ServerSocket servers = null;
            Socket       fromclient = null;

            // create server socket
            try {
                servers = new ServerSocket(4444);
            } catch (IOException e) {
                System.out.println("Couldn't listen to port 4444");
                System.exit(-1);
            }

            try {
                System.out.print("Waiting for a client...");
                fromclient= servers.accept();
                System.out.println("ClientBad connected");
            } catch (IOException e) {
                System.out.println("Can't accept");
                System.exit(-1);
            }

            in  = new BufferedReader(new
                    InputStreamReader(fromclient.getInputStream()));
            out = new PrintWriter(fromclient.getOutputStream(),true);

            String input;
            String output;

            System.out.println("Wait for messages");
            while ((input = in.readLine()) != null) {
                if (input.equalsIgnoreCase("exit")) break;
                System.out.println("User input is " + input);
                out.println("S ::: \nsdfsdfsdsdf\nsfsdfdsf\n#");
                System.out.println(input);
            }
            out.close();
            in.close();
            fromclient.close();
            servers.close();
        }
}
