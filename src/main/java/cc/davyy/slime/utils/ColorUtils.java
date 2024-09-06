package cc.davyy.slime.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.TextDecoration;
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

import static net.minestom.server.MinecraftServer.LOGGER;

/**
 * Utility class for handling color and text formatting using Adventure's MiniMessage and ANSI serialization.
 */
public final class ColorUtils {

    private static final MiniMessage MINIMESSAGE = MiniMessage.miniMessage();
    private static final ANSIComponentSerializer ANSI_SERIALIZER = ANSIComponentSerializer.builder().build();
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

    public static void print(@NotNull Component component) {
        String ansiString = ANSI_SERIALIZER.serialize(component);
        LOGGER.info(ansiString);
    }

    public static @NotNull Component txt(@NotNull String message) {
        return MINIMESSAGE.deserialize(message).decoration(TextDecoration.ITALIC, false);
    }

    public static void broadcastAllInstances(@NotNull String message) {
        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player ->
                player.sendMessage(of(message).parseLegacy().build()));
    }

    public static void broadcastSingleInstance(@NotNull String message, @NotNull Instance targetInstance) {
        targetInstance.getPlayers().forEach(player ->
                player.sendMessage(of(message).parseLegacy().build()));
    }

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