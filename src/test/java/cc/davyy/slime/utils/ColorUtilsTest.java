package cc.davyy.slime.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorUtilsTest {

    @Test
    void testOf() {
        ColorUtils colorUtils = ColorUtils.of("Hello");
        assertEquals("Hello", colorUtils.toString());
    }

    @Test
    void testBuild() {
        ColorUtils colorUtils = ColorUtils.of("Hello");
        Component helloComponent = colorUtils.build()
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);

        Component expectedComponent = Component.text("Hello")
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);

        assertEquals(expectedComponent, helloComponent);
    }

    @Test
    void testParseLegacy() {
        ColorUtils colorUtils = ColorUtils.of("&aHello").parseLegacy();
        Component result = colorUtils.build()
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);

        Component expectedComponent = Component.text("Hello", NamedTextColor.GREEN)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);

        assertEquals(expectedComponent, result);
    }

}