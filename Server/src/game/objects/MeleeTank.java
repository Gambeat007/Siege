package game.objects;


import game.Player;

public class MeleeTank extends Unit {

    public MeleeTank(Player owner, String race) {
        super(owner, race,15,30,90,3,2);
    }

    @Override
    public String toString() {
        return "tank" + "," + super.toString();
    }
}
