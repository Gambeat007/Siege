package game.objects;

import game.Player;

import java.util.concurrent.ThreadLocalRandom;

public class Unit extends GameObject {
    private String race;
    private int maxAttackDmg;
    private int minAttackDmg;
    private int healthPoints;
    private int moveRadius;
    private int shootRadius;
    private Player owner;

    public Unit() {
        moveRadius = 1;
        shootRadius = 1;
        race = "human";
    }

    public Unit(Player owner, String race, int minAttackDmg, int maxAttackDmg, int healthPoints, int moveRadius, int shootRadius) {
        this.race = race;
        this.maxAttackDmg = maxAttackDmg;
        this.minAttackDmg = minAttackDmg;
        this.healthPoints = healthPoints;
        this.moveRadius = moveRadius;
        this.shootRadius = shootRadius;
        this.owner = owner;
    }

    public int getShootRadius() {
        return shootRadius;
    }

    public int getMoveRadius() {
        return moveRadius;
    }

    public Player getOwner() {
        return owner;
    }

    public String getRace() {
        return race;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getMaxAttackDmg() {
        return maxAttackDmg;
    }
    public int getMinAttackDmg() {
        return minAttackDmg;
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
