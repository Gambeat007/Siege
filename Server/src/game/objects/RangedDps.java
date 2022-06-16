package game.objects;

import game.Player;

public class RangedDps extends Unit {

    public RangedDps(Player owner, String race) {
        super(owner, race,25,30,50,4,4);
    }

    @Override
    public String toString() {
        return "archer" + "," + super.toString();
    }
}