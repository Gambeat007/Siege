package game.objects;

import game.Player;

public class Obstacle extends Unit {

    public Obstacle(Player owner, String race) {
        super(owner, race,0,0, 30,0,0);
    }

    @Override
    public String toString() {
        return "obstacle" + "," + super.toString();
    }
}
