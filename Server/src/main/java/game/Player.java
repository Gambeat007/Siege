package game;

import lombok.Getter;
import lombok.Setter;

import java.net.Socket;
import java.util.Objects;

@Getter
public class Player {
    private Socket socket;
    private int playerId;
    @Setter
    private String playerName;

    public void setSocket(Socket socket) {
        this.socket = socket;
        this.playerId = socket.getPort();
        this.playerName = "tmp" + this.playerId;
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
