package server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {
    private Socket player1;
    private Socket player2;
    private int sessionNumber;

    public ServerApp() {

    }

    public void startRunning() {

        try (ServerSocket serverSocket = new ServerSocket(9001)) {

            while(true) {
                System.out.println("Waiting for first client...");
                player1 = serverSocket.accept();
                System.out.println("Player1 connected: " + player1);

                System.out.println("Waiting for second client...");
                player2 = serverSocket.accept();
                System.out.println("Player2 connected: " + player2);

                SessionHandler clientThread = new SessionHandler(player1, player2);

                clientThread.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}
