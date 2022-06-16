package game;

import game.objects.GameObject;
import game.objects.Unit;

import java.util.ArrayList;

public class Hexagon {


    private int i;
    private int j;
    private boolean isFilled;
    private GameObject gameObject;
    private Unit unit;


    public Hexagon(int i, int j) {
        this.i = i;
        this.j = j;
        isFilled = false;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean filled) {
        this.isFilled = filled;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public Unit getUnit() {
        return unit;
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
