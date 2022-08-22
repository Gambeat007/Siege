package game;

import game.objects.Unit;

public class Hexagon {

    private int i;
    private int j;
    private boolean isFilled;
    private Unit unit;

    public Hexagon(int i, int j) {
        this.i = i;
        this.j = j;
        isFilled = false;
    }

    public Unit getUnit() {
        return unit;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
        this.isFilled = true;
    }

    public void clear() {
        this.unit = null;
        this.isFilled = false;
    }

    public void move(Hexagon from) {
        isFilled = true;
        this.unit = from.getUnit();
    }

    @Override
    public String toString() {
        return isFilled + "," + unit;
    }
}
