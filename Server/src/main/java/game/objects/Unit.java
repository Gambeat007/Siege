package game.objects;

import game.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@AllArgsConstructor
public class Unit {
    private Player owner;
    private String race;
    private int minAttackDmg;
    private int maxAttackDmg;
    private int healthPoints;
    private int moveRadius;
    private int shootRadius;

    public Unit() {
        moveRadius = 1;
        shootRadius = 1;
        race = "human";
    }

    public Unit(Player owner, String race) {
        this.owner = owner;
        this.race = race;
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
