package cc.davyy.slime.utils;

import cc.davyy.slime.model.SlimePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static cc.davyy.slime.utils.ColorUtils.*;

public final class GeneralUtils {

    private GeneralUtils() {}

    /**
     * Broadcasts a message to all players across all instances in the server.
     *
     * @param message the message to broadcast, in the form of a {@link Component}
     */
    public static void broadcastAllInstances(@NotNull Component message) {
        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player ->
                player.sendMessage(message));
    }

    /**
     * Broadcasts a message to all players in a specified instance.
     *
     * @param message the message to broadcast, in the form of a {@link Component}
     * @param targetInstance the target {@link Instance} where the message will be sent
     */
    public static void broadcastSingleInstance(@NotNull Component message, @NotNull Instance targetInstance) {
        targetInstance.getPlayers().forEach(player ->
                player.sendMessage(message));
    }

    /**
     * Generates a random integer between the specified minimum and maximum values.
     *
     * @param min the minimum value of the range
     * @param max the maximum value of the range (inclusive)
     * @return a random integer between the specified range
     */
    public static int getRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * Retrieves a collection of all currently online {@link SlimePlayer}s.
     *
     * @return a {@link Collection} of all online {@link SlimePlayer}s
     */
    public static Collection<SlimePlayer> getOnlineSlimePlayers() {
        return MinecraftServer.getConnectionManager().getOnlinePlayers().stream()
                .filter(player -> player instanceof SlimePlayer)
                .map(player -> (SlimePlayer) player)
                .toList();
    }

    /**
     * Creates a translatable component using the specified translation key.
     *
     * @param key the translation key (e.g., "chat.type.text")
     * @return a {@link TranslatableComponent} created with the specified key
     */
    public static TranslatableComponent translateComponent(@NotNull String key) {
        return Component.translatable(key);
    }

    /**
     * Creates a translatable component with arguments using the specified translation key.
     *
     * @param key the translation key (e.g., "chat.type.text")
     * @param args the components to be used as arguments in the translation
     * @return a {@link TranslatableComponent} created with the specified key and arguments
     */
    public static TranslatableComponent translatable(@NotNull String key, @NotNull Component... args) {
        return Component.translatable(key, args);
    }

    /**
     * Sends a translatable message to the given {@link CommandSender}.
     *
     * @param sender the {@link CommandSender} who will receive the message
     * @param key the translation key (e.g., "chat.type.text")
     * @param args the components to be used as arguments in the translation
     */
    public static void sendTranslatable(@NotNull CommandSender sender, @NotNull String key, @NotNull Component... args) {
        sender.sendMessage(translatable(key, args));
    }

    /**
     * Sends a colorable text message to the specified {@link CommandSender}, allowing for MiniMessage formatting.
     *
     * @param sender the {@link CommandSender} who will receive the message
     * @param text the message text in MiniMessage format
     */
    public static void sendColorable(@NotNull CommandSender sender, @NotNull String text) {
        sender.sendMessage(txt(text));
    }

    /**
     * Sends a raw {@link Component} message to the specified {@link CommandSender}.
     *
     * @param sender the {@link CommandSender} who will receive the message
     * @param text the {@link Component} to be sent
     */
    public static void sendComponent(@NotNull CommandSender sender, @NotNull Component text) {
        sender.sendMessage(text);
    }

    /**
     * Checks if the given {@link CommandSender} has the specified permission.
     *
     * @param sender the {@link CommandSender} to check for permission
     * @param permission the permission to check (e.g., "myplugin.use")
     * @return {@code true} if the sender has the specified permission, {@code false} otherwise
     */
    public static boolean hasPlayerPermission(@NotNull CommandSender sender, @NotNull String permission) {
        return sender instanceof SlimePlayer player && player.hasPermission(permission);
    }

    public static @NotNull ItemStack createItem(@NotNull Material material, @NotNull String name, @NotNull List<String> lore) {
        return ItemStack.builder(material)
                .customName(of(name)
                        .build())
                .lore(stringListToComponentList(lore))
                .build();
    }

}