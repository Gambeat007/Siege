package game.objects;

import game.Player;
import lombok.Getter;
import lombok.Setter;
import java.util.concurrent.ThreadLocalRandom;

public class Unit {
    @Setter @Getter private Player owner;
    @Setter @Getter private String race;
    @Setter @Getter private int minAttackDmg;
    @Setter @Getter private int maxAttackDmg;
    @Setter @Getter private int healthPoints;
    @Setter @Getter private int moveRadius;
    @Setter @Getter private int shootRadius;

    public Unit() {
        moveRadius = 1;
        shootRadius = 1;
        race = "human";
    }

    public Unit(Player owner, String race) {
        this.owner = owner;
        this.race = race;
    }

    public Unit(Player owner, String race, int minAttackDmg, int maxAttackDmg, int healthPoints, int moveRadius, int shootRadius) {
        this.owner = owner;
        this.race = race;
        this.minAttackDmg = minAttackDmg;
        this.maxAttackDmg = maxAttackDmg;
        this.healthPoints = healthPoints;
        this.moveRadius = moveRadius;
        this.shootRadius = shootRadius;
    }

    public void attack(Unit attacker) {
        this.healthPoints -= ThreadLocalRandom.current().nextInt(attacker.getMinAttackDmg(), attacker.getMaxAttackDmg() + 1);
    }

    public boolean isDead() {
        if (healthPoints <= 0)
            return true;

        return false;
    }

    @Override
    public String toString() {
        return race + "," + maxAttackDmg + "," + healthPoints + "," + moveRadius + "," + shootRadius + "," + owner.getSocket().getPort();
    }
}
