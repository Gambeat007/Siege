package game.objects;

import game.Player;

public class MeleeTank extends Unit {

    public MeleeTank(Player owner, String race) {
        super(owner, race);
        switch (race) {
            case "human":
                this.setMinAttackDmg(15);
                this.setMaxAttackDmg(30);
                this.setHealthPoints(90);
                this.setMoveRadius(3);
                this.setShootRadius(2);
                break;
            case "goblin":
                this.setMinAttackDmg(30);
                this.setMaxAttackDmg(45);
                this.setHealthPoints(60);
                this.setMoveRadius(4);
                this.setShootRadius(2);
                break;
            case "monster":
                this.setMinAttackDmg(15);
                this.setMaxAttackDmg(25);
                this.setHealthPoints(100);
                this.setMoveRadius(2);
                this.setShootRadius(2);
                break;
            default:
                this.setMinAttackDmg(0);
                this.setMaxAttackDmg(0);
                this.setHealthPoints(0);
                this.setMoveRadius(0);
                this.setShootRadius(0);
        }
    }

    @Override
    public String toString() {
        return "tank" + "," + super.toString();
    }
}
