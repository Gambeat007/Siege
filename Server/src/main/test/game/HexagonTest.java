package game;

import game.objects.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HexagonTest {
    Hexagon hexagon;

    @BeforeEach
    public void setup() {
        hexagon = new Hexagon(1,1);
    }

    @Test
    public void shouldCreateHexagon() {
        assertNotNull(hexagon);
    }

    @Test
    public void shouldClear() {
        hexagon.clear();

        assertFalse(hexagon.isFilled());
    }

    @Test
    public void shouldMove() {
        Unit unit = new Unit();
        Hexagon hexagonTo = new Hexagon(2,2);

        hexagon.setUnit(unit);
        hexagonTo.move(hexagon);
        hexagon.clear();

        assertFalse(hexagon.isFilled());
        assertTrue(hexagonTo.isFilled());
    }

}
