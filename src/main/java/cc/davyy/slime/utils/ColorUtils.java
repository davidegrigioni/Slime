package cc.davyy.slime.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for handling color and text formatting using Adventure's MiniMessage and ANSI serialization.
 */
public final class ColorUtils {

    private static final MiniMessage MINIMESSAGE = MiniMessage.miniMessage();
    private static final Pattern LEGACY_REGEX = Pattern.compile("[§&][0-9a-fk-or]");

    private static final Map<String, String> LEGACY_TO_MINIMESSAGE = createLegacyMap();

    private final List<TagResolver> minimessagePlaceholders = new ArrayList<>();
    private String text;

    private ColorUtils() {}

    public static @NotNull ColorUtils of(String text) {
        return new ColorUtils().setText(text);
    }

    public @NotNull Component build() {
        return MINIMESSAGE.deserialize(getText(), minimessagePlaceholders.toArray(new TagResolver[0]))
                .decoration(TextDecoration.ITALIC, false);
    }

    public @NotNull ColorUtils parseLegacy() {
        String textParsed = getText();
        Matcher matcher = LEGACY_REGEX.matcher(textParsed);

        while (matcher.find()) {
            String match = matcher.group();
            textParsed = textParsed.replace(match, LEGACY_TO_MINIMESSAGE.getOrDefault(match, match));
        }

        setText(textParsed);
        return this;
    }

    public @NotNull ColorUtils parseMMP(@Subst("test_pl") @NotNull String placeholder, @NotNull ComponentLike value) {
        minimessagePlaceholders.add(Placeholder.component(placeholder, value));
        return this;
    }

    public @NotNull ColorUtils parseMMP(@Subst("test_pl") @NotNull String placeholder, @NotNull String value) {
        minimessagePlaceholders.add(Placeholder.component(placeholder, of(value).parseLegacy().build()));
        return this;
    }

    public static @NotNull List<Component> stringListToComponentList(@NotNull List<String> strings) {
        List<Component> lore = new ArrayList<>();
        for (String line : strings) {
            lore.add(MINIMESSAGE.deserialize(line).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        }
        return lore;
    }

    public static @NotNull Component txt(@NotNull String message) {
        return MINIMESSAGE.deserialize(message).decoration(TextDecoration.ITALIC, false);
    }

    // 1. Insert a component placeholder
    public static Component formatWithComponent(@NotNull String template, @Subst("test_pl") @NotNull String placeholder, @NotNull Component component) {
        return MINIMESSAGE.deserialize(template, Placeholder.component(placeholder, component));
    }

    /*
     * Example usage:
     * Component message = MiniMessageUtils.formatWithComponent("<gray>Hello <name>!", "name", Component.text("John", NamedTextColor.BLUE));
     * // Result: "Hello John!" where John is blue.
     */

    // 2. Insert unparsed text placeholder
    public static Component formatWithUnparsed(@NotNull String template, @Subst("test_pl") @NotNull String placeholder, @NotNull String unparsedText) {
        return MINIMESSAGE.deserialize(template, Placeholder.unparsed(placeholder, unparsedText));
    }

    /*
     * Example usage:
     * Component message = MiniMessageUtils.formatWithUnparsed("<gray>Hello <name>", "name", "<red>John :)");
     * // Result: "Hello <red>John :)" as plain text, no parsing for the name.
     */

    // 3. Insert parsed text placeholder
    public static Component formatWithParsed(@NotNull String template, @Subst("test_pl") @NotNull String placeholder, @NotNull String parsedText) {
        return MINIMESSAGE.deserialize(template, Placeholder.parsed(placeholder, parsedText));
    }

    /*
     * Example usage:
     * Component message = MiniMessageUtils.formatWithParsed("<gray>Hello <name>!", "name", "<red>John");
     * // Result: "Hello John!" where John is red.
     */

    // 4. Insert custom styling with a placeholder
    public static Component formatWithStyle(@NotNull String template, @Subst("test_pl") @NotNull String styleTag, @NotNull TextColor color, @NotNull ClickEvent clickEvent) {
        return MINIMESSAGE.deserialize(template, Placeholder.styling(styleTag, clickEvent, color));
    }

    /*
     * Example usage:
     * Component message = MiniMessageUtils.formatWithStyle("<my-style>Click me!</my-style>", "my-style", NamedTextColor.GREEN, ClickEvent.runCommand("/hello"));
     * // Result: Green "Click me!" text that runs the `/hello` command when clicked.
     */

    // 5. Insert a formatted number placeholder
    public static Component formatWithNumber(@NotNull String template, @Subst("test_pl") @NotNull String placeholder, double number) {
        return MINIMESSAGE.deserialize(template, Formatter.number(placeholder, number));
    }

    /*
     * Example usage:
     * Component message = MiniMessageUtils.formatWithNumber("<gray>Your balance is <balance>.", "balance", 150.75, "#.00");
     * // Result: "Your balance is 150.75" with the number formatted.
     */

    // 6. Insert a formatted date placeholder
    public static Component formatWithDate(@NotNull String template, @Subst("test_pl") @NotNull String placeholder, @NotNull LocalDateTime date) {
        return MINIMESSAGE.deserialize(template, Formatter.date(placeholder, date));
    }

    /*
     * Example usage:
     * Component message = MiniMessageUtils.formatWithDate("<gray>Current date is: <date>.", "date", LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss");
     * // Result: "Current date is: 2024-09-08 14:55:32" (formatted date)
     */

    // 7. Insert a choice-based placeholder (for dynamic text based on number)
    public static Component formatWithChoice(@NotNull String template, @Subst("test_pl") @NotNull String placeholder, int number) {
        return MINIMESSAGE.deserialize(template, Formatter.choice(placeholder, number));
    }

    /*
     * Example usage:
     * Component message = MiniMessageUtils.formatWithChoice("<gray>I met <choice>!", "choice", 5, "0#no developer|1#one developer|1<many developers");
     * // Result: "I met many developers!" since the input number was 5.
     */

    /*public static final StringTemplate.Processor<Component, RuntimeException> MM = stringTemplate -> {
        String interpolated = STR.process(stringTemplate);
        return MINIMESSAGE.deserialize(interpolated).decoration(TextDecoration.ITALIC, false);
    };*/

    @Override
    public String toString() {
        return getText();
    }

    private String getText() {
        return text;
    }

    private @NotNull ColorUtils setText(String text) {
        this.text = text;
        return this;
    }

    private static Map<String, String> createLegacyMap() {
        Map<String, String> legacyToMiniMessage = new HashMap<>();
        legacyToMiniMessage.put("§0", "<black>");
        legacyToMiniMessage.put("§1", "<dark_blue>");
        legacyToMiniMessage.put("§2", "<dark_green>");
        legacyToMiniMessage.put("§3", "<dark_aqua>");
        legacyToMiniMessage.put("§4", "<dark_red>");
        legacyToMiniMessage.put("§5", "<dark_purple>");
        legacyToMiniMessage.put("§6", "<gold>");
        legacyToMiniMessage.put("§7", "<gray>");
        legacyToMiniMessage.put("§8", "<dark_gray>");
        legacyToMiniMessage.put("§9", "<blue>");
        legacyToMiniMessage.put("§a", "<green>");
        legacyToMiniMessage.put("§b", "<aqua>");
        legacyToMiniMessage.put("§c", "<red>");
        legacyToMiniMessage.put("§d", "<light_purple>");
        legacyToMiniMessage.put("§e", "<yellow>");
        legacyToMiniMessage.put("§f", "<white>");
        legacyToMiniMessage.put("§k", "<obfuscated>");
        legacyToMiniMessage.put("§l", "<bold>");
        legacyToMiniMessage.put("§m", "<strikethrough>");
        legacyToMiniMessage.put("§n", "<underlined>");
        legacyToMiniMessage.put("§o", "<italic>");
        legacyToMiniMessage.put("§r", "<reset>");
        legacyToMiniMessage.put("&0", "<black>");
        legacyToMiniMessage.put("&1", "<dark_blue>");
        legacyToMiniMessage.put("&2", "<dark_green>");
        legacyToMiniMessage.put("&3", "<dark_aqua>");
        legacyToMiniMessage.put("&4", "<dark_red>");
        legacyToMiniMessage.put("&5", "<dark_purple>");
        legacyToMiniMessage.put("&6", "<gold>");
        legacyToMiniMessage.put("&7", "<gray>");
        legacyToMiniMessage.put("&8", "<dark_gray>");
        legacyToMiniMessage.put("&9", "<blue>");
        legacyToMiniMessage.put("&a", "<green>");
        legacyToMiniMessage.put("&b", "<aqua>");
        legacyToMiniMessage.put("&c", "<red>");
        legacyToMiniMessage.put("&d", "<light_purple>");
        legacyToMiniMessage.put("&e", "<yellow>");
        legacyToMiniMessage.put("&f", "<white>");
        legacyToMiniMessage.put("&k", "<obfuscated>");
        legacyToMiniMessage.put("&l", "<bold>");
        legacyToMiniMessage.put("&m", "<strikethrough>");
        legacyToMiniMessage.put("&n", "<underlined>");
        legacyToMiniMessage.put("&o", "<italic>");
        legacyToMiniMessage.put("&r", "<reset>");
        return legacyToMiniMessage;
    }

}