package cc.davyy.slime.utils;

import cc.davyy.slime.model.SlimePlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.command.CommandSender;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

import static cc.davyy.slime.utils.ColorUtils.*;

public final class GeneralUtils {

    private GeneralUtils() {}

    /**
     * Broadcasts a message to all players across all instances in the server.
     *
     * @param message the message to broadcast, in the form of a {@link Component}
     */
    public static void broadcastAllInstances(@NotNull Component message) {
        Audiences.players().sendMessage(message);
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