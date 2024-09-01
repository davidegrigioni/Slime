package cc.davyy.slime.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ColorUtilsTest {

    private static final MiniMessage MINIMESSAGE = MiniMessage.builder().strict(true).build();

    @Test
    void testOf() {
        ColorUtils colorUtils = ColorUtils.of("Hello");
        assertNotNull(colorUtils);
        assertEquals("Hello", colorUtils.toString());
    }

    @Test
    void testBuild() {
        String text = "<red>Hello</red>";
        ColorUtils colorUtils = ColorUtils.of(text);
        Component component = colorUtils.build();
        Component expected = MINIMESSAGE.deserialize(text)
                .decoration(TextDecoration.ITALIC, false);

        assertEquals(expected, component);
    }

    @Test
    void testTxtLore() {
        String loreString = "Line 1\nLine 2\nLine 3";
        List<Component> loreComponents = ColorUtils.txtLore(loreString);

        assertEquals(3, loreComponents.size());
        assertEquals(MINIMESSAGE.deserialize("Line 1").decoration(TextDecoration.ITALIC, false), loreComponents.get(0));
        assertEquals(MINIMESSAGE.deserialize("Line 2").decoration(TextDecoration.ITALIC, false), loreComponents.get(1));
        assertEquals(MINIMESSAGE.deserialize("Line 3").decoration(TextDecoration.ITALIC, false), loreComponents.get(2));
    }

    @Test
    void testToString() {
        String text = "Hello";
        ColorUtils colorUtils = ColorUtils.of(text);
        assertEquals(text, colorUtils.toString());
    }

}