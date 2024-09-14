package cc.davyy.slime.interfaces;

import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface IBroadcast {

    void broadcastMessage(@NotNull CommandSender sender, @NotNull String message);

    void broadcastTitle(@NotNull CommandSender sender, @NotNull String titleText, @NotNull Collection<SlimePlayer> players);

    void broadcastTitle(@NotNull CommandSender sender, @NotNull String titleText, @NotNull String subTitle, @NotNull Collection<SlimePlayer> players);

    void broadcastTitleWithTimes(@NotNull CommandSender sender, @NotNull String titleText, @NotNull String subTitle, @NotNull Collection<SlimePlayer> players);

}