package game;

import java.net.Socket;
import java.util.Objects;

public class Player {
    private Socket socket;
    private int playerId;
    private String playerName;

    public void setSocket(Socket socket) {
        this.socket = socket;
        this.playerId = socket.getPort();
        this.playerName = "tmp" + this.playerId;
    }

    public Socket getSocket() {
        return socket;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return playerId == player.playerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId);
    }
}
