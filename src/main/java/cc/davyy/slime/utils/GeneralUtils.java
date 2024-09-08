package cc.davyy.slime.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import static cc.davyy.slime.utils.ColorUtils.txt;

public final class GeneralUtils {

    private GeneralUtils() {}

    public static void broadcastAllInstances(@NotNull Component message) {
        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player ->
                player.sendMessage(message));
    }

    public static void broadcastSingleInstance(@NotNull Component message, @NotNull Instance targetInstance) {
        targetInstance.getPlayers().forEach(player ->
                player.sendMessage(message));
    }

    public static int getRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static Collection<Player> getOnlinePlayers() {
        return MinecraftServer.getConnectionManager().getOnlinePlayers();
    }

    public static TranslatableComponent translateComponent(@NotNull String key) {
        return Component.translatable(key);
    }

    public static TranslatableComponent translatable(@NotNull String key, @NotNull Component... args) {
        return Component.translatable(key, args);
    }

    public static void sendTranslatable(@NotNull Player player, @NotNull String key, @NotNull Component... args) {
        player.sendMessage(translatable(key, args));
    }

    public static void sendColorable(@NotNull Player player, @NotNull String text) {
        player.sendMessage(txt(text));
    }

    public static void sendComponent(@NotNull Player player, @NotNull Component text) {
        player.sendMessage(text);
    }

}