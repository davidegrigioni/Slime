package cc.davyy.slime.utils;

import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessagesTest {

    @Test
    void testPosPlaceholder() {
        Component playerName = Component.text("TestPlayer");
        String pos = PosUtils.toString(PosUtils.of(10, 10, 10));

        String message = "Spawn set by <player_name> at <pos>";

        Component result = ColorUtils.of(message)
                .parseMMP("player_name", playerName)
                .parseMMP("pos", pos)
                .build();

        Component expected = Component.text("Spawn set by TestPlayer at 10,10,10");

        assertEquals(expected, result, "The placeholder was not replaced correctly.");
    }

}