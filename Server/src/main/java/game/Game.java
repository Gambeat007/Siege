package game;

import game.objects.*;
import java.net.Socket;
import java.util.*;
import static game.Properties.BOARD_HEIGHT;
import static game.Properties.BOARD_WIDTH;
import static game.Properties.MOVES_IN_A_ROUND;

public class Game {
    Hexagon[][] hexagons;
    private Map<Class<? extends Unit>, Integer> unitDeployMap = new HashMap<>();
    private int armyCount = 0;
    private int player1Army;
    private int player2Army;
    private int movesLeftInRound;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Player nullPlayer;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.nullPlayer = new Player();
        nullPlayer.setSocket(new Socket());
        this.currentPlayer = player1;
        this.movesLeftInRound = MOVES_IN_A_ROUND;
        this.hexagons = new Hexagon[BOARD_HEIGHT][BOARD_WIDTH];
        this.setUnitDeployBalance();
        initializeHexagons();
    }

    private void setUnitDeployBalance() {
        unitDeployMap.put(MeleeTank.class, 2);
        unitDeployMap.put(MeleeDps.class, 3);
        unitDeployMap.put(RangedDps.class, 3);
        unitDeployMap.put(BuilderDps.class, 1);
    }

    private void createUnitType (Player owner, String army, int i, int j, Map.Entry<Class<? extends Unit>, Integer> unitStartAmount ) {
        if (unitStartAmount.getKey() == MeleeTank.class) {
            hexagons[i][j].setUnit(new MeleeTank(owner, army));
            unitDeployMap.put(MeleeTank.class, unitDeployMap.get(MeleeTank.class)-1);
        } else if (unitStartAmount.getKey() == RangedDps.class) {
            hexagons[i][j].setUnit(new RangedDps(owner, army));
            unitDeployMap.put(RangedDps.class, unitDeployMap.get(RangedDps.class)-1);
        } else if (unitStartAmount.getKey() == MeleeDps.class) {
            hexagons[i][j].setUnit(new MeleeDps(owner, army));
            unitDeployMap.put(MeleeDps.class, unitDeployMap.get(MeleeDps.class)-1);
        } else if (unitStartAmount.getKey() == BuilderDps.class) {
            hexagons[i][j].setUnit(new BuilderDps(owner, army));
            unitDeployMap.put(BuilderDps.class, unitDeployMap.get(BuilderDps.class) - 1);
        }
    }

    public void initializeHexagons() {
        for (int i = 0; i < hexagons.length; i++) {
            for (int j = 0; j < hexagons[i].length; j++) {
                hexagons[i][j] = new Hexagon(i, j);
            }
        }
    }

    public void generateArmy(Player owner, String army) {
        armyCount++;
        int x = BOARD_HEIGHT;
        int y = armyCount > 1 ? BOARD_WIDTH - 3 : 1;
        int chestPlacement = 0;

        if (armyCount == 1) {
            chestPlacement = 0;
            player1Army = 9;
        } else  if (armyCount == 2) {
            chestPlacement = BOARD_WIDTH - 1;
            player2Army = 9;
        }
        List<String> chestContent = new ArrayList<>();
        List<Integer> pawnPlaces = drawWithoutRepetition();
        int currentIteration = 0;

        for (int i = 0; i < x; i++) {
            for (int j = y; j < y + 2; j++) {

                if (pawnPlaces.contains(currentIteration)) {
                    Optional<Map.Entry<Class<? extends Unit>, Integer>> first = unitDeployMap.entrySet().stream().filter(entry -> entry.getValue() > 0).findFirst();

                    if (first.isPresent()) {
                        Map.Entry<Class<? extends Unit>, Integer> unitStartAmount = first.get();
                        createUnitType(owner, army, i, j, unitStartAmount);

                    } else {
                        break;
                    }
                }
                currentIteration++;
            }
        }
        this.setUnitDeployBalance();

        chestContent.add("diamond");
        chestContent.add("coal");
        chestContent.add("coal");
        Collections.shuffle(chestContent);

        for (int i = 0; i < 3; i++) {
            hexagons[i*4][chestPlacement].setUnit(new Chest(owner, chestContent.get(i)));
        }
    }

    public List<Integer> drawWithoutRepetition() {
        ArrayList<Integer> list = new ArrayList<>(18);
        for (int i = 0; i < 18; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        return list.subList(0,9);
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public boolean attackAndContinue(String attacker, String victim) {
        System.out.println("attacking");
        String[] attackerTokens = attacker.split(",");
        String[] victimTokens = victim.split(",");

        Hexagon attackerHex = hexagons[Integer.parseInt(attackerTokens[1])][Integer.parseInt(attackerTokens[2])];
        Hexagon victimHex = hexagons[Integer.parseInt(victimTokens[1])][Integer.parseInt(victimTokens[2])];



        victimHex.getUnit().attack(attackerHex.getUnit());

        if (victimHex.getUnit().isDead()) {
            if (victimHex.getUnit() instanceof Chest) {
                ((Chest) victimHex.getUnit()).open();
                if (((Chest) victimHex.getUnit()).isOpen() && victimHex.getUnit().getRace().equals("diamond")) {
                    return false;
                }
            } else {
                if (victimHex.getUnit().getOwner().equals(player1)) {
                    player1Army--;
                } else if (victimHex.getUnit().getOwner().equals(player2)) {
                    player2Army--;
                }

                victimHex.clear();

                if (player1Army == 0 || player2Army == 0) {
                    return false;
                }
            }
        }

        return true;
    }

    public void move(String from, String to) {
        System.out.println("moving");
        String[] fromTokens = from.split(",");
        String[] toTokens = to.split(",");

        hexagons[Integer.parseInt(toTokens[1])][Integer.parseInt(toTokens[2])].move(hexagons[Integer.parseInt(fromTokens[1])][Integer.parseInt(fromTokens[2])]);
        hexagons[Integer.parseInt(fromTokens[1])][Integer.parseInt(fromTokens[2])].clear();
    }

    public void build(String builder, String buildingSite) {
        System.out.println("building");
        String[] buildingSiteTokens = buildingSite.split(",");

        hexagons[Integer.parseInt(buildingSiteTokens[1])][Integer.parseInt(buildingSiteTokens[2])].setUnit(new Obstacle(nullPlayer, "other"));
    }

    public void countTurn(int playerId) {
        if (movesLeftInRound > 0 && playerId == currentPlayer.getPlayerId()) {
            movesLeftInRound--;
            if (movesLeftInRound == 0) {
                if (currentPlayer.getPlayerId() == player1.getPlayerId()) {
                    currentPlayer = player2;
                } else {
                    currentPlayer = player1;
                }
                movesLeftInRound = MOVES_IN_A_ROUND;
            }
        }
    }

    @Override
    public String toString() {
        String msg = "";
        for (Hexagon[] hexes: hexagons) {
            for (Hexagon hex: hexes) {
                msg += hex.toString() + ";";
            }
        }

        return msg;
    }
}
