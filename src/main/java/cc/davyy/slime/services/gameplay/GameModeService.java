package cc.davyy.slime.services.gameplay;

import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.entity.GameMode;
import org.jetbrains.annotations.NotNull;

/**
 * Service interface for managing game modes for players.
 */
public interface GameModeService {

    /**
     * Sets the game mode for a specific player.
     * <p>
     * This method changes the game mode of the given player to the specified game mode.
     * The change is applied to the player themselves.
     * </p>
     *
     * @param player the player whose game mode will be changed
     * @param gameMode the new game mode to set for the player
     */
    void setGameMode(@NotNull SlimePlayer player, @NotNull GameMode gameMode);

    /**
     * Sets the game mode for a target player on behalf of another player.
     * <p>
     * This method changes the game mode of the target player to the specified game mode.
     * The change is applied as if the action was performed by the given source player.
     * </p>
     *
     * @param player the player performing the action (often for logging or permissions)
     * @param gameMode the new game mode to set for the target player
     * @param target the player whose game mode will be changed
     */
    void setGameMode(@NotNull SlimePlayer player, @NotNull GameMode gameMode, @NotNull SlimePlayer target);

}