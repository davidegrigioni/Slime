package cc.davyy.slime.managers;

import cc.davyy.slime.utils.Messages;
import com.google.inject.Singleton;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

@Singleton
public class GameModeManager {

    public void setGameMode(@NotNull Player player, @NotNull GameMode gameMode) {
        player.setGameMode(gameMode);
        player.sendMessage(Messages.GAMEMODE_SWITCH
                .addPlaceholder("gamemode", gameMode.name())
                .asComponent());
    }

    public void setGameMode(@NotNull Player player, @NotNull GameMode gameMode, @NotNull Player target) {
        target.setGameMode(gameMode);
        player.sendMessage(Messages.GAMEMODE_SWITCH_OTHER_SELF
                .addPlaceholder("target", target.getName())
                .addPlaceholder("gamemode", gameMode.name())
                .asComponent());
        target.sendMessage(Messages.GAMEMODE_SWITCH_OTHER
                .addPlaceholder("gamemode", gameMode.name())
                .asComponent());
    }

    @NotNull
    public GameMode getGameMode(@NotNull Player player) {
        return player.getGameMode();
    }

    public void resetGameMode(@NotNull Player player) {
        setGameMode(player, GameMode.SURVIVAL);
        player.sendMessage("Your game mode has been reset to Survival.");
    }

}