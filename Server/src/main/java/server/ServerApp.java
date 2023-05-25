package server;

import lombok.NoArgsConstructor;

import java.net.ServerSocket;
import java.net.Socket;

@NoArgsConstructor
public class ServerApp {
    private Socket player1;
    private Socket player2;

    public void startRunning() {
        try (ServerSocket serverSocket = new ServerSocket(9001)) {
            while (true) {
                System.out.println("Waiting for first player...");
                player1 = serverSocket.accept();
                System.out.println("Player1 connected: " + player1);
                System.out.println("Waiting for second player...");
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
