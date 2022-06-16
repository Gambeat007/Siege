package pw.client;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

import java.io.IOException;
import java.util.ArrayList;

public class Hexagon {

    private int i;
    private int j;
    private Polygon hex;
    private boolean isActive;
    private boolean isFilled;
    private boolean isBlocker;
    private String unitName;
    private String unitRace;
    private String unitClass;
    private String unitType;
    private int attackDamage;
    private int healthPoints;
    private int moveRadius;
    private int attackRange;
    private ArrayList<Hexagon> moveNeighbours;
    private ArrayList<Hexagon> shootNeighbours;
    StringBuilder url;
    String owner;

    public Hexagon(Polygon hex, int i, int j) {
        this.i = i;
        this.j = j;
        this.hex = hex;
        this.isActive = false;
        this.isFilled = false;
        this.isBlocker = false;
        this.moveNeighbours = new ArrayList<>();
        this.shootNeighbours = new ArrayList<>();
    }

    public void populate(String type, String race, int attack, int healthPoints, int moveRadius, int shootRadius, String owner, int clientPort) throws IOException {
        url = new StringBuilder(System.getProperty("user.dir") + "\\src\\main\\resources\\client\\pawns\\");
        switch (race) {
            case "goblin":
                url.append("goblin\\");
                break;
            case "human":
                url.append("human\\");
                break;
            case "monster":
                url.append("monster\\");
                break;
            default:
                url.append("other\\");
                break;
        }

        switch (type) {
            case "soldier":
                url.append("sword.png");
                break;
            case "archer":
                url.append("bow.png");
                break;
            case "tank":
                url.append("shield.png");
                break;
            case "builder":
                url.append("hammer.png");
                break;
            case "obstacle":
                url.append("obstacle.png");
                this.isBlocker = true;
                break;
            case "chest":

                if (owner.equals(clientPort + "")) {
                    if (race.equals("coal")) {
                        url.append("chestCoal.png");
                    } else if (race.equals("diamond")) {
                        url.append("chestDiamond.png");
                    }
                } else {
                    url.append("chest.png");
                }
                break;
            case "openChest":
                if (race.equals("coal")) {
                    url.append("chestCoal.png");
                } else if (race.equals("diamond")) {
                    url.append("chestDiamond.png");
                } else {
                    url.append("chest.png");
                }
                break;
        }

        this.hex.setFill(new ImagePattern(new Image(url.toString())));
        this.isFilled = true;
        this.attackDamage = attack;
        this.healthPoints = healthPoints;
        this.unitType = type;
        this.unitRace = race;
        this.moveRadius = moveRadius;
        this.attackRange = shootRadius;
        this.owner = owner;
    }

    public void addNeighbour(Hexagon neighbour) {
        moveNeighbours.add(neighbour);
    }

    public void addShootNeighbours(Hexagon shootNeighbour) {
        shootNeighbours.add(shootNeighbour);
    }

    public ArrayList<Hexagon> getNeighbours() {
        return moveNeighbours;
    }

    public ArrayList<Hexagon> getShootNeighbours() {
        return shootNeighbours;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public boolean isBlocker() {
        return isBlocker;
    }

    public void setIsActive(boolean active) {

        if (active) {
            this.isActive = true;
            url = url.replace(url.lastIndexOf(".png"), url.lastIndexOf(".png"), "_selected");

            this.hex.setFill(new ImagePattern(new Image(url.toString())));
            for (Hexagon hex: moveNeighbours) {
                if (!hex.isFilled()) {
                    hex.getHex().setFill(Color.GRAY);
                    hex.getHex().opacityProperty().set(0.5);
                }
            }
            for (Hexagon hex: shootNeighbours) {
                hex.getHex().setStroke(Color.RED);
            }
        } else {
            if (isFilled && isActive) {
                if (url.indexOf("_selected") != -1)
                    url = url.replace(url.lastIndexOf("_selected"), url.lastIndexOf("_selected") + 9, "");
                this.hex.setFill(new ImagePattern(new Image(url.toString())));
                this.hex.setStroke(Color.rgb(77,77,77,0.33));
                for (Hexagon hex: shootNeighbours) {
                    hex.getHex().setStroke(Color.rgb(77,77,77,0.33));
                }
                for (Hexagon hex: moveNeighbours) {
                    if (!hex.isFilled()) {
                        hex.getHex().setFill(Color.TRANSPARENT);
                        hex.getHex().opacityProperty().set(1);
                    }
                }
            } else {
                this.hex.setFill(Color.TRANSPARENT);
                this.hex.setStroke(Color.rgb(77,77,77,0.33));
                this.hex.opacityProperty().set(1);
            }
            this.isActive = false;
        }
    }

    public Polygon getHex() {
        return this.hex;
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }

    public void setBlocker(boolean blocker) {
        isBlocker = blocker;
    }

    public String getType() {
        return unitType;
    }

    public String getRace() {
        return unitRace;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getMoveRadius() {
        return moveRadius;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public String getOwner() {
        return owner;
    }

    public String getUnitName() {
        return unitName;
    }

    public String getUnitRace() {
        return unitRace;
    }

    public String getUnitClass() {
        return unitClass;
    }

    public String getUnitType() {
        return unitType;
    }

    @Override
    public String toString() {
        if (isFilled) {
            return isFilled + "," + i + "," + j + "," + unitType + "," + unitRace + "," + attackDamage + "," + healthPoints + "," + moveRadius + "," + attackRange + "," + owner;
        } else {
            return isFilled + "," + i + "," + j;
        }

    }
}
