package cc.davyy.slime.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.ansi.ANSIComponentSerializer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

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

    // MiniMessage instance for processing text with MiniMessage syntax.
    private static final MiniMessage MINIMESSAGE = MiniMessage.miniMessage();

    // ANSI serializer for converting components into ANSI escape codes for terminal output.
    private static final ANSIComponentSerializer ANSI_SERIALIZER = ANSIComponentSerializer.builder()
            .build();

    // Logger for outputting components as text in the console.
    private static final ComponentLogger COMPONENT_LOGGER = ComponentLogger.logger();

    private static final Pattern LEGACY_REGEX = Pattern.compile("[§&][0-9a-fk-or]");
    private static final Map<String, String> LEGACY_TO_MINIMESSAGE;
    static {
        LEGACY_TO_MINIMESSAGE = new HashMap<>();
        LEGACY_TO_MINIMESSAGE.put("§0", "<black>");
        LEGACY_TO_MINIMESSAGE.put("§1", "<dark_blue>");
        LEGACY_TO_MINIMESSAGE.put("§2", "<dark_green>");
        LEGACY_TO_MINIMESSAGE.put("§3", "<dark_aqua>");
        LEGACY_TO_MINIMESSAGE.put("§4", "<dark_red>");
        LEGACY_TO_MINIMESSAGE.put("§5", "<dark_purple>");
        LEGACY_TO_MINIMESSAGE.put("§6", "<gold>");
        LEGACY_TO_MINIMESSAGE.put("§7", "<gray>");
        LEGACY_TO_MINIMESSAGE.put("§8", "<dark_gray>");
        LEGACY_TO_MINIMESSAGE.put("§9", "<blue>");
        LEGACY_TO_MINIMESSAGE.put("§a", "<green>");
        LEGACY_TO_MINIMESSAGE.put("§b", "<aqua>");
        LEGACY_TO_MINIMESSAGE.put("§c", "<red>");
        LEGACY_TO_MINIMESSAGE.put("§d", "<light_purple>");
        LEGACY_TO_MINIMESSAGE.put("§e", "<yellow>");
        LEGACY_TO_MINIMESSAGE.put("§f", "<white>");
        LEGACY_TO_MINIMESSAGE.put("§k", "<obfuscated>");
        LEGACY_TO_MINIMESSAGE.put("§l", "<bold>");
        LEGACY_TO_MINIMESSAGE.put("§m", "<strikethrough>");
        LEGACY_TO_MINIMESSAGE.put("§n", "<underlined>");
        LEGACY_TO_MINIMESSAGE.put("§o", "<italic>");
        LEGACY_TO_MINIMESSAGE.put("§r", "<reset>");
        LEGACY_TO_MINIMESSAGE.put("&0", "<black>");
        LEGACY_TO_MINIMESSAGE.put("&1", "<dark_blue>");
        LEGACY_TO_MINIMESSAGE.put("&2", "<dark_green>");
        LEGACY_TO_MINIMESSAGE.put("&3", "<dark_aqua>");
        LEGACY_TO_MINIMESSAGE.put("&4", "<dark_red>");
        LEGACY_TO_MINIMESSAGE.put("&5", "<dark_purple>");
        LEGACY_TO_MINIMESSAGE.put("&6", "<gold>");
        LEGACY_TO_MINIMESSAGE.put("&7", "<gray>");
        LEGACY_TO_MINIMESSAGE.put("&8", "<dark_gray>");
        LEGACY_TO_MINIMESSAGE.put("&9", "<blue>");
        LEGACY_TO_MINIMESSAGE.put("&a", "<green>");
        LEGACY_TO_MINIMESSAGE.put("&b", "<aqua>");
        LEGACY_TO_MINIMESSAGE.put("&c", "<red>");
        LEGACY_TO_MINIMESSAGE.put("&d", "<light_purple>");
        LEGACY_TO_MINIMESSAGE.put("&e", "<yellow>");
        LEGACY_TO_MINIMESSAGE.put("&f", "<white>");
        LEGACY_TO_MINIMESSAGE.put("&k", "<obfuscated>");
        LEGACY_TO_MINIMESSAGE.put("&l", "<bold>");
        LEGACY_TO_MINIMESSAGE.put("&m", "<strikethrough>");
        LEGACY_TO_MINIMESSAGE.put("&n", "<underlined>");
        LEGACY_TO_MINIMESSAGE.put("&o", "<italic>");
        LEGACY_TO_MINIMESSAGE.put("&r", "<reset>");
    }

    // List of MiniMessage placeholders for dynamic text processing.
    private final List<TagResolver> minimessagePlaceholders = new ArrayList<>();
    // The raw text to be processed.
    private String text;

    // Private constructor to prevent direct instantiation.
    private ColorUtils() {}

    /**
     * Static factory method to create a new instance of ColorUtils with the specified text.
     *
     * @param text The text to be processed.
     * @return A new instance of ColorUtils.
     */
    public static @NotNull ColorUtils of(String text) { return new ColorUtils().setText(text); }

    /**
     * Builds and returns the processed text as a Component.
     *
     * @return The processed Component with the applied MiniMessage placeholders and formatting.
     */
    public @NotNull Component build() { return MINIMESSAGE.deserialize(getText(), this.minimessagePlaceholders.toArray(new TagResolver[0])).decoration(TextDecoration.ITALIC, false); }

    /**
     * Parse legacy color codes and formatting, including <code>{@literal &}</code> and
     * <code>{@literal §}</code> into their minimessage equivalents.
     *
     * @return the color parser object
     */
    public @NotNull ColorUtils parseLegacy() {
        String textParsed = getText();
        final @NotNull Matcher matcher = LEGACY_REGEX.matcher(textParsed);

        while (matcher.find()) {
            final String match = matcher.group();
            textParsed = textParsed.replace(match, LEGACY_TO_MINIMESSAGE.getOrDefault(match, match));
        }

        setText(textParsed);

        return this;
    }

    /**
     * Adds a MiniMessage placeholder to the current instance with a Component value.
     *
     * @param placeholder The name of the placeholder.
     * @param value The Component value for the placeholder.
     * @return The current ColorUtils instance with the added placeholder.
     */
    public @NotNull ColorUtils parseMMP(@Subst("test_placeholder") @NotNull String placeholder, @NotNull ComponentLike value) {
        this.minimessagePlaceholders.add(
                Placeholder.component(placeholder, value)
        );
        return this;
    }

    /**
     * Adds a MiniMessage placeholder to the current instance with a String value.
     *
     * @param placeholder The name of the placeholder.
     * @param value The String value for the placeholder.
     * @return The current ColorUtils instance with the added placeholder.
     */
    public @NotNull ColorUtils parseMMP(@Subst("test_placeholder") @NotNull String placeholder, @NotNull String value) {
        this.minimessagePlaceholders.add(
                Placeholder.component(placeholder, of(value).parseLegacy().build())
        );
        return this;
    }

    /**
     * Converts a newline-separated string into a list of Components, each representing a line of text.
     *
     * @param strings The string to be split into lines and converted.
     * @return A list of Components, each corresponding to a line in the input string.
     */
    public static List<Component> stringListToComponentList(List<String> strings) {
        List<Component> lore = new ArrayList<>();
        for (String line : strings) {
            lore.add(MINIMESSAGE.deserialize(line).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        }
        return lore;
    }

    /**
     * Prints a colored and formatted message to the console.
     *
     * @param component The Adventure Component to print.
     */
    public static void print(@NotNull Component component) {
        final String ansiString = ANSI_SERIALIZER.serialize(component);
        COMPONENT_LOGGER.info(ansiString);
    }

    /**
     * Parses a string with MiniMessage syntax into a Component.
     *
     * @param message The message to parse.
     * @return A Component representing the parsed message.
     */
    public static Component txt(@NotNull String message) { return MINIMESSAGE.deserialize(message).decoration(TextDecoration.ITALIC, false); }

    public static void broadcastAllInstances(@NotNull String message) {
        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player ->
                player.sendMessage(of(message)
                        .parseLegacy()
                        .build()));
    }

    public static void broadcastSingleInstance(@NotNull String message, @NotNull Instance targetInstance) {
        targetInstance.getPlayers().forEach(player ->
                player.sendMessage(of(message)
                        .parseLegacy()
                        .build()));
    }

    @Override
    public String toString() { return getText(); }

    private String getText() { return text; }

    private @NotNull ColorUtils setText(String text) {
        this.text = text;
        return this;
    }

}