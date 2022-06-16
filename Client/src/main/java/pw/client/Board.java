package pw.client;

import javafx.scene.shape.Polygon;

import java.io.IOException;

public class Board {

    Hexagon[][] hexagons;

    public Board(Hexagon[][] hexagons) {
        this.hexagons = hexagons;
    }

    public void parseMessage(String[] hexes, int clientPort) throws IOException {

        int k = 0;
        for (int i = 0; i < hexagons.length; i++) {
            for (int j = 0; j < hexagons[i].length; j++) {
                String[] tokens = hexes[k].split(",");
                if (tokens[0].equals("true")) {
                    hexagons[i][j].populate(tokens[1], tokens[2], Integer.valueOf(tokens[3]), Integer.valueOf(tokens[4]), Integer.valueOf(tokens[5]), Integer.valueOf(tokens[6]), tokens[7], clientPort);
                } else {
                    hexagons[i][j].setFilled(false);
                    hexagons[i][j].setIsActive(false);
                    hexagons[i][j].setBlocker(false);
                }
                k++;
            }
        }

        int l = 0;
        for (int i = 0; i < hexagons.length; i++) {
            for (int j = 0; j < hexagons[i].length; j++) {
                String[] tokens = hexes[l].split(",");
                if (tokens[0].equals("true")) {
                    addMoveNeighbours(hexagons[i][j], hexagons[i][j].getMoveRadius(), i, j);
                    addShootNeighbours(hexagons[i][j], hexagons[i][j].getAttackRange(), i, j);
                }
                l++;
            }
        }
    }

    public boolean isAnyHexActive() {

        for (int i = 0; i < hexagons.length; i++) {
            for (int j = 0; j < hexagons[i].length; j++) {
                if (hexagons[i][j].isActive()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void deactivateAll() {
        for (int i = 0; i < hexagons.length; i++) {
            for (int j = 0; j < hexagons[i].length; j++) {
                if (hexagons[i][j].isActive()) {
                    hexagons[i][j].setIsActive(false);
                }
            }
        }
    }

    public void activate (String currentPort, Polygon hex) {
        for (int i = 0; i < hexagons.length; i++) {
            for (int j = 0; j < hexagons[i].length; j++) {
                if (hexagons[i][j].getHex().getId().equals(hex.getId()) && hexagons[i][j].getOwner().equals(currentPort)) {
                    hexagons[i][j].setIsActive(true);
                }
            }
        }
    }

    public boolean isOpponent (String currentPort, Polygon hex) {
        for (int i = 0; i < hexagons.length; i++) {
            for (int j = 0; j < hexagons[i].length; j++) {
                if (hexagons[i][j].getHex().getId().equals(hex.getId()) && !hexagons[i][j].getOwner().equals(currentPort)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isActiveNeighbour (Polygon hex) {
        for (int i = 0; i < hexagons.length; i++) {
            for (int j = 0; j < hexagons[i].length; j++) {
                if (hexagons[i][j].isActive()) {
                    for (Hexagon neighbour: hexagons[i][j].getNeighbours()) {
                        if (neighbour.getHex().getId().equals(hex.getId())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isShootingActiveNeighbour (Polygon hex) {
        for (int i = 0; i < hexagons.length; i++) {
            for (int j = 0; j < hexagons[i].length; j++) {
                if (hexagons[i][j].isActive()) {
                    for (Hexagon neighbour: hexagons[i][j].getShootNeighbours()) {
                        if (neighbour.getHex().getId().equals(hex.getId())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isFilledIn (Polygon hex) {
        for (int i = 0; i < hexagons.length; i++) {
            for (int j = 0; j < hexagons[i].length; j++) {
                if (hexagons[i][j].getHex().getId().equals(hex.getId()) && hexagons[i][j].isFilled()) {
                    return true;
                }
            }
        }
        return false;
    }

    public String[] getUnitStats(Polygon hex) {
        for (int i = 0; i < hexagons.length; i++) {
            for (int j = 0; j < hexagons[i].length; j++) {
                if (hexagons[i][j].getHex().getId().equals(hex.getId())) {

                    String[] unitStats = {String.valueOf(hexagons[i][j].getHealthPoints()),
                            String.valueOf(hexagons[i][j].getMoveRadius()), String.valueOf(hexagons[i][j].getAttackDamage()),
                            String.valueOf(hexagons[i][j].getAttackRange())};
                    return unitStats;
                }
            }
        }
        return null;
    }

    public boolean isBlocker (Polygon hex) {
        for (int i = 0; i < hexagons.length; i++) {
            for (int j = 0; j < hexagons[i].length; j++) {
                if (hexagons[i][j].getHex().getId().equals(hex.getId()) && hexagons[i][j].isBlocker()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canBuild() {
        for (int i = 0; i < hexagons.length; i++) {
            for (int j = 0; j < hexagons[i].length; j++) {
                if (hexagons[i][j].isFilled() && hexagons[i][j].isActive()) {
                    if (hexagons[i][j].getType().equals("builder")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public String takeAction(String actionType, Polygon hex) {
        Hexagon origin = hexagons[0][0];
        Hexagon destination = hexagons[0][0];

        for (int i = 0; i < hexagons.length; i++) {
            for (int j = 0; j < hexagons[i].length; j++) {
                if (hexagons[i][j].isFilled() && hexagons[i][j].isActive()) {
                    origin = hexagons[i][j];
                }
                if (hexagons[i][j].getHex().getId().equals(hex.getId())) {
                    destination = hexagons[i][j];
                }
            }
        }

        return actionType + "/" + origin + "/" + destination;
    }

    public void addMoveNeighbours(Hexagon hex, int radius, int i, int j) {

        int maxI = hexagons.length;
        int maxJ = hexagons[0].length;

        if (radius >= 1) {
            if (i % 2 == 0) {
                if (i - 1 >= 0 && !hexagons[i - 1][j].isFilled()) {
                    hex.addNeighbour(hexagons[i - 1][j]);
                    addMoveNeighbours(hex, radius-1, i-1, j);
                    if (j - 1 >= 0 && !hexagons[i - 1][j - 1].isFilled()) {
                        hex.addNeighbour(hexagons[i - 1][j - 1]);
                        addMoveNeighbours(hex, radius-1, i-1, j-1);
                    }
                }
                if (i + 1 < maxI  && !hexagons[i + 1][j].isFilled()) {
                    hex.addNeighbour(hexagons[i + 1][j]);
                    addMoveNeighbours(hex, radius-1, i+1, j);
                    if (j - 1 >= 0 && !hexagons[i + 1][j - 1].isFilled()) {
                        hex.addNeighbour(hexagons[i + 1][j - 1]);
                        addMoveNeighbours(hex, radius-1, i+1, j-1);
                    }
                }
                if (j - 1 >= 0 && !hexagons[i][j - 1].isFilled()) {
                    hex.addNeighbour(hexagons[i][j - 1]);
                    addMoveNeighbours(hex, radius-1, i, j-1);
                }
                if (j + 1 < maxJ && !hexagons[i][j + 1].isFilled()) {
                    hex.addNeighbour(hexagons[i][j + 1]);
                    addMoveNeighbours(hex, radius-1, i, j+1);
                }
            } else {
                if (i - 1 >= 0 && !hexagons[i - 1][j].isFilled()) {
                    hex.addNeighbour(hexagons[i - 1][j]);
                    addMoveNeighbours(hex, radius-1, i-1, j);
                    if (j + 1 < maxJ && !hexagons[i - 1][j + 1].isFilled()) {
                        hex.addNeighbour(hexagons[i - 1][j + 1]);
                        addMoveNeighbours(hex, radius-1, i-1, j+1);
                    }
                }
                if (i + 1 < maxI && !hexagons[i + 1][j].isFilled()) {
                    hex.addNeighbour(hexagons[i + 1][j]);
                    addMoveNeighbours(hex, radius-1, i+1, j);
                    if (j + 1 < maxJ && !hexagons[i + 1][j + 1].isFilled()) {
                        hex.addNeighbour(hexagons[i + 1][j + 1]);
                        addMoveNeighbours(hex, radius-1, i+1, j+1);
                    }
                }
                if (j - 1 >= 0 && !hexagons[i][j - 1].isFilled()) {
                    hex.addNeighbour(hexagons[i][j - 1]);
                    addMoveNeighbours(hex, radius-1, i, j-1);
                }
                if (j + 1 < maxJ && !hexagons[i][j + 1].isFilled()) {
                    hex.addNeighbour(hexagons[i][j + 1]);
                    addMoveNeighbours(hex, radius-1, i, j+1);
                }
            }
        }
    }

    public void addShootNeighbours(Hexagon hex, int radius, int i, int j) {

        int maxI = hexagons.length;
        int maxJ = hexagons[0].length;

        if (radius >= 1) {
            if (i % 2 == 0) {
                if (i - 1 >= 0) {
                    hex.addShootNeighbours(hexagons[i - 1][j]);
                    if (!hexagons[i - 1][j].isBlocker())
                        addShootNeighbours(hex, radius-1, i-1, j);
                    if (j - 1 >= 0) {
                        hex.addShootNeighbours(hexagons[i - 1][j - 1]);
                        if (!hexagons[i - 1][j - 1].isBlocker())
                            addShootNeighbours(hex, radius-1, i-1, j-1);
                    }
                }
                if (i + 1 < maxI) {
                    hex.addShootNeighbours(hexagons[i + 1][j]);
                    if (!hexagons[i + 1][j].isBlocker())
                        addShootNeighbours(hex, radius-1, i+1, j);
                    if (j - 1 >= 0) {
                        hex.addShootNeighbours(hexagons[i + 1][j - 1]);
                        if (!hexagons[i + 1][j - 1].isBlocker())
                            addShootNeighbours(hex, radius-1, i+1, j-1);
                    }
                }
                if (j - 1 >= 0) {
                    hex.addShootNeighbours(hexagons[i][j - 1]);
                    if (!hexagons[i][j - 1].isBlocker())
                        addShootNeighbours(hex, radius-1, i, j-1);
                }
                if (j + 1 < maxJ) {
                    hex.addShootNeighbours(hexagons[i][j + 1]);
                    if (!hexagons[i][j + 1].isBlocker())
                        addShootNeighbours(hex, radius-1, i, j+1);
                }
            } else {
                if (i - 1 >= 0) {
                    hex.addShootNeighbours(hexagons[i - 1][j]);
                    if (!hexagons[i - 1][j].isBlocker())
                        addShootNeighbours(hex, radius-1, i-1, j);
                    if (j + 1 < maxJ) {
                        hex.addShootNeighbours(hexagons[i - 1][j + 1]);
                        if (!hexagons[i - 1][j + 1].isBlocker())
                            addShootNeighbours(hex, radius-1, i-1, j+1);
                    }
                }
                if (i + 1 < maxI) {
                    hex.addShootNeighbours(hexagons[i + 1][j]);
                    if (!hexagons[i + 1][j].isBlocker())
                        addShootNeighbours(hex, radius-1, i+1, j);
                    if (j + 1 < maxJ) {
                        hex.addShootNeighbours(hexagons[i + 1][j + 1]);
                        if (!hexagons[i + 1][j + 1].isBlocker())
                            addShootNeighbours(hex, radius-1, i+1, j+1);
                    }
                }
                if (j - 1 >= 0) {
                    hex.addShootNeighbours(hexagons[i][j - 1]);
                    if (!hexagons[i][j - 1].isBlocker())
                        addShootNeighbours(hex, radius-1, i, j-1);
                }
                if (j + 1 < maxJ) {
                    hex.addShootNeighbours(hexagons[i][j + 1]);
                    if (!hexagons[i][j + 1].isBlocker())
                        addShootNeighbours(hex, radius-1, i, j+1);
                }
            }
        }
    }
}
