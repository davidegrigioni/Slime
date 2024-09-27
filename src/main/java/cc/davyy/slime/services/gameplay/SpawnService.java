package cc.davyy.slime.services.gameplay;

import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Service interface for managing spawn points and teleportation in the game.
 */
public interface SpawnService {

    /**
     * Sets the spawn position to the specified coordinates.
     * <p>
     * This method updates the spawn position to the given coordinates.
     * All players will be teleported to this position when they use the
     * teleportation functionality.
     * </p>
     *
     * @param pos the new spawn position to set.
     */
    void setSpawnPosition(@NotNull Pos pos);

    /**
     * Retrieves the current spawn position.
     * <p>
     * This method returns the coordinates of the current spawn position.
     * If no spawn position has been set, this method will return null.
     * </p>
     *
     * @return the current spawn position, or null if no spawn position is set.
     */
    @Nullable Pos getSpawnPosition();

    /**
     * Teleports the specified player to the current spawn position.
     * <p>
     * This method will move the given player to the spawn position. If no spawn position
     * is set, the player will not be teleported.
     * </p>
     *
     * @param player the player to teleport to the spawn position.
     */
    void teleportToSpawn(@NotNull SlimePlayer player);

}