package game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.net.Socket;

@Getter
@EqualsAndHashCode
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
}
