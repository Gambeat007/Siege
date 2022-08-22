package game.objects;

import game.Player;

public class RangedDps extends Unit {

    public RangedDps(Player owner, String race) {
        super(owner, race);
        switch (race) {
            case "human":
                this.setMinAttackDmg(25);
                this.setMaxAttackDmg(40);
                this.setHealthPoints(50);
                this.setMoveRadius(4);
                this.setShootRadius(4);
                break;
            case "goblin":
                this.setMinAttackDmg(20);
                this.setMaxAttackDmg(35);
                this.setHealthPoints(40);
                this.setMoveRadius(4);
                this.setShootRadius(4);
                break;
            case "monster":
                this.setMinAttackDmg(30);
                this.setMaxAttackDmg(45);
                this.setHealthPoints(60);
                this.setMoveRadius(3);
                this.setShootRadius(4);
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
        return "archer" + "," + super.toString();
    }
}