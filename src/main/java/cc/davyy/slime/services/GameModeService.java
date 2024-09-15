package cc.davyy.slime.services;

import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.entity.GameMode;
import org.jetbrains.annotations.NotNull;

public interface GameModeService {

    void setGameMode(@NotNull SlimePlayer player, @NotNull GameMode gameMode);

    void setGameMode(@NotNull SlimePlayer player, @NotNull GameMode gameMode, @NotNull SlimePlayer target);

}