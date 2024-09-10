package cc.davyy.slime.utils;

import cc.davyy.slime.model.SlimePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
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

    public static Collection<SlimePlayer> getOnlineSlimePlayers() {
        return MinecraftServer.getConnectionManager().getOnlinePlayers().stream()
                .filter(player -> player instanceof SlimePlayer)
                .map(player -> (SlimePlayer) player)
                .toList();
    }

    public static TranslatableComponent translateComponent(@NotNull String key) {
        return Component.translatable(key);
    }

    public static TranslatableComponent translatable(@NotNull String key, @NotNull Component... args) {
        return Component.translatable(key, args);
    }

    public static void sendTranslatable(@NotNull CommandSender sender, @NotNull String key, @NotNull Component... args) {
        sender.sendMessage(translatable(key, args));
    }

    public static void sendColorable(@NotNull CommandSender sender, @NotNull String text) {
        sender.sendMessage(txt(text));
    }

    public static void sendComponent(@NotNull CommandSender sender, @NotNull Component text) {
        sender.sendMessage(text);
    }

    public static boolean hasPlayerPermission(@NotNull CommandSender sender, @NotNull String permission) {
        return sender instanceof SlimePlayer player && player.hasPermission(permission);
    }

}