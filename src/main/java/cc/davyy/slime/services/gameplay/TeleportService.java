package cc.davyy.slime.services.gameplay;

import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;

/**
 * Service interface for handling player teleportation.
 */
public interface TeleportService {

    /**
     * Teleports the specified player to the location of the target player.
     * <p>
     * This method moves the given player to the current location of the target player.
     * Both players must be valid and online for the teleportation to succeed.
     * </p>
     *
     * @param player the player to teleport.
     * @param target the player whose location the first player will be teleported to.
     */
    void teleportPlayerToTarget(@NotNull SlimePlayer player, @NotNull SlimePlayer target);

    /**
     * Teleports the specified player to the given coordinates.
     * <p>
     * This method moves the player to a specific set of coordinates in the game world.
     * </p>
     *
     * @param player the player to teleport.
     */
    void teleportPlayerToCoordinates(@NotNull SlimePlayer player, @NotNull Pos position);

    /**
     * Teleports the target player to the location of the executor.
     * <p>
     * This method moves the target player to the current location of the executor.
     * Both players must be valid and online for the teleportation to succeed.
     * </p>
     *
     * @param executor the player initiating the teleport (often an admin or command executor).
     * @param target the player who will be teleported to the executor's location.
     */
    void teleportTargetToExecutor(@NotNull SlimePlayer executor, @NotNull SlimePlayer target);

}