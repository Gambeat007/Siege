package game.objects;

import game.Player;

public class Chest extends Unit {
    private String name;
    private boolean isOpen;

    public Chest(Player owner, String race) {
        super(owner, race,0,0, 120,0,0);
        this.name = "chest";
        this.isOpen = false;
    }

    public void open() {
        this.name = "openChest";
        this.isOpen = true;
    }

    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public String toString() {
        return this.name + "," + super.toString();
    }
}
