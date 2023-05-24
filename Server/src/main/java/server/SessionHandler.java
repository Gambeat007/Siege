package server;

import game.Game;
import game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SessionHandler extends Thread {
    private Player player1;
    private Player player2;
    private BufferedReader reader1;
    private BufferedReader reader2;
    private PrintWriter writer1;
    private PrintWriter writer2;
    private Game game;
    private boolean endgame;

    public SessionHandler(Socket socket1, Socket socket2) {
        try {
            this.endgame = false;
            this.player1 = new Player();
            this.player2 = new Player();
            this.player1.setSocket(socket1);
            this.player2.setSocket(socket2);
            this.game = new Game(player1, player2);

            this.reader1 = new BufferedReader(new InputStreamReader(player1.getSocket().getInputStream()));
            this.reader2 = new BufferedReader(new InputStreamReader(player2.getSocket().getInputStream()));
            this.writer1 = new PrintWriter(player1.getSocket().getOutputStream(), true);
            this.writer2 = new PrintWriter(player2.getSocket().getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Runnable r = () -> doRun(player1, reader1, writer1, writer2);
        Runnable r2 = () -> doRun(player2, reader2, writer1, writer2);
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
    }

    private void doRun(Player player, BufferedReader bufferedReader, PrintWriter writer1, PrintWriter writer2) {
        try {
            String message;
            while ((message = bufferedReader.readLine()) != null) {
                String response;
                String[] tokens = message.split(":");
                if (tokens[0].equals("chat")) {
                    writer1.println(message);
                    writer2.println(message);
                } else {
                    if (tokens[0].equals("first")) {
                        game.generateArmy(player, tokens[1]);
                    } else {
                        player.setPlayerName(tokens[0]);
                        game.countTurn(player.getPlayerId());
                        splitTokens(tokens);
                    }
                    if (endgame) {
                        response = "endgame:" + game.getCurrentPlayer().getSocket().getPort();
                    } else {
                        response = game.getCurrentPlayer().getSocket().getPort() + ":" + game.toString();
                    }
                    System.out.println("response: " + response);
                    writer1.println(response);
                    writer2.println(response);
                }
            }
        } catch (Exception e) {
            System.out.println("Connection with player: " + player.toString() + " lost.");
        } finally {
            try {
                bufferedReader.close();
                writer1.close();
                writer2.close();
                player.getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void splitTokens(String[] tokens) {
        String[] actionTokens = tokens[1].split("/");
        switch (actionTokens[0]) {
            case "attack":
                if (!game.attackAndContinue(actionTokens[1], actionTokens[2])) {
                    endgame = true;
                }
                break;
            case "move":
                game.move(actionTokens[1], actionTokens[2]);
                break;
            case "build":
                game.build(actionTokens[1], actionTokens[2]);
                break;
        }
    }
}
