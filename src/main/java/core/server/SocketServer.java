package core.server;

import core.Commander;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Arthur Kupriyanov
 */
public class SocketServer {
    private static ServerSocket server; // серверсокет
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет
    private static final int PORT = 4004;

    public static void main(String[] args) throws IOException {
            try {
                server = new ServerSocket(PORT);
                System.out.println("SocketServer started on port: " + PORT);
                Socket clientSocket = null;
                while(true) {
                    try {
                        clientSocket = server.accept();
                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                        String word = in.readLine();
                        System.out.println(word);
                        out.write(Commander.getProgramResponse(word));
                        out.flush();


                    } finally {
                        if (clientSocket != null) {
                            clientSocket.close();
                        }
                        in.close();
                        out.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Сервер закрыт!");
                server.close();
            }
    }
}
