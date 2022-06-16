package game.objects;

import game.Player;

public class MeleeDps extends Unit {

    public MeleeDps(Player owner, String race) {
        super(owner, race, 35,50, 60,3,2);
    }

    @Override
    public String toString() {
        return "soldier" + "," + super.toString();
    }
}
