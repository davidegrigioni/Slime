package cc.davyy.slime.utils;

import net.minestom.server.coordinate.Pos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PosUtilsTest {

    @Test
    void testToString() {
        Pos pos = new Pos(100.5, 64.0, -200.75, 90.0f, 0.0f);
        String expected = "100.5,64.0,-200.75,90.0,0.0";
        String result = PosUtils.toString(pos);

        assertEquals(expected, result, "The position was not converted to string correctly.");
    }

    @Test
    void testFromString() {
        String posString = "100.5,64.0,-200.75,90.0,0.0";
        Pos expected = new Pos(100.5, 64.0, -200.75, 90.0f, 0.0f);
        Pos result = PosUtils.fromString(posString);

        assertEquals(expected, result, "The string was not converted to position correctly.");
    }

    @Test
    void testFromStringInvalidFormat() {
        String invalidString = "100.5,64.0";

        assertThrows(IllegalArgumentException.class, () -> {
            PosUtils.fromString(invalidString);
        }, "Expected an IllegalArgumentException to be thrown for invalid format.");
    }

    @Test
    void testFromStringWithOptionalYawPitch() {
        String posString = "100.5,64.0,-200.75";
        Pos expected = new Pos(100.5, 64.0, -200.75);
        Pos result = PosUtils.fromString(posString);

        assertEquals(expected, result, "The string with no yaw and pitch was not converted to position correctly.");
    }

}