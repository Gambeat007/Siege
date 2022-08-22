package game.objects;

import game.Player;

public class MeleeDps extends Unit {

    public MeleeDps(Player owner, String race) {
        super(owner, race);
        switch (race) {
            case "human" -> {
                this.setMinAttackDmg(35);
                this.setMaxAttackDmg(50);
                this.setHealthPoints(60);
                this.setMoveRadius(3);
                this.setShootRadius(2);
            }
            case "goblin" -> {
                this.setMinAttackDmg(20);
                this.setMaxAttackDmg(30);
                this.setHealthPoints(50);
                this.setMoveRadius(5);
                this.setShootRadius(1);
            }
            case "monster" -> {
                this.setMinAttackDmg(40);
                this.setMaxAttackDmg(60);
                this.setHealthPoints(70);
                this.setMoveRadius(3);
                this.setShootRadius(1);
            }
            default -> {
                this.setMinAttackDmg(0);
                this.setMaxAttackDmg(0);
                this.setHealthPoints(0);
                this.setMoveRadius(0);
                this.setShootRadius(0);
            }
        }
    }

    @Override
    public String toString() {
        return "soldier" + "," + super.toString();
    }
}
