package game.objects;

import game.Player;

public class BuilderDps extends Unit {

    public BuilderDps(Player owner, String race) {
        super(owner, race, 5, 15, 50, 4, 1);
    }

    @Override
    public String toString() {
        return "builder" + "," + super.toString();
    }
}
