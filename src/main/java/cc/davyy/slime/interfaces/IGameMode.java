package cc.davyy.slime.interfaces;

import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.entity.GameMode;
import org.jetbrains.annotations.NotNull;

public interface IGameMode {

    void setGameMode(@NotNull SlimePlayer player, @NotNull GameMode gameMode);

    void setGameMode(@NotNull SlimePlayer player, @NotNull GameMode gameMode, @NotNull SlimePlayer target);

}