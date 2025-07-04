package cc.davyy.slime.managers.gameplay;

import cc.davyy.slime.services.gameplay.GameModeService;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.model.Messages;
import com.google.inject.Singleton;
import net.minestom.server.entity.GameMode;
import org.jetbrains.annotations.NotNull;

@Singleton
public class GameModeManager implements GameModeService {

    @Override
    public void setGameMode(@NotNull SlimePlayer player, @NotNull GameMode gameMode) {
        player.setGameMode(gameMode);
        player.sendMessage(Messages.GAMEMODE_SWITCH
                .addPlaceholder("gamemode", gameMode.name())
                .asComponent());
    }

    @Override
    public void setGameMode(@NotNull SlimePlayer player, @NotNull GameMode gameMode, @NotNull SlimePlayer target) {
        target.setGameMode(gameMode);
        player.sendMessage(Messages.GAMEMODE_SWITCH_OTHER_SELF
                .addPlaceholder("target", target.getName())
                .addPlaceholder("gamemode", gameMode.name())
                .asComponent());
        target.sendMessage(Messages.GAMEMODE_SWITCH_OTHER
                .addPlaceholder("gamemode", gameMode.name())
                .asComponent());
    }

}