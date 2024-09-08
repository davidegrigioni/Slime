package cc.davyy.slime.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
    void testToString() {
        String text = "Hello";
        ColorUtils colorUtils = ColorUtils.of(text);
        assertEquals(text, colorUtils.toString());
    }

    @Test
    void testSendPlaceholders() {
        String text = "<red>Hello, <player>!";
        Component playerComponent = Component.text("JohnDoe").color(NamedTextColor.RED);

        // Act
        Component result = ColorUtils.of(text)
                .parseMMP("player", playerComponent)
                .build();

        // Expected result
        Component expected = MINIMESSAGE.deserialize("<red>Hello, <player>!",
                        Placeholder.component("player", playerComponent))
                .decoration(TextDecoration.ITALIC, false);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void testSendPlaceholdersWithMultipleValues() {
        String text = "<red>Hello, <player>! You have <count> messages.";
        Component playerComponent = Component.text("JohnDoe").color(NamedTextColor.RED);
        Component countComponent = Component.text("5").color(NamedTextColor.GREEN);

        // Act
        Component result = ColorUtils.of(text)
                .parseMMP("player", playerComponent)
                .parseMMP("count", countComponent)
                .build();

        // Expected result
        Component expected = MINIMESSAGE.deserialize("<red>Hello, <player>! You have <count> messages.",
                        Placeholder.component("player", playerComponent),
                        Placeholder.component("count", countComponent))
                .decoration(TextDecoration.ITALIC, false);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void testSendPlaceholdersWithEmptyText() {
        String text = "";

        // Act
        Component result = ColorUtils.of(text).build();

        // Expected result
        Component expected = Component.empty();

        // Assert
        assertEquals(expected, result);
    }

}