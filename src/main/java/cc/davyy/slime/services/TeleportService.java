package cc.davyy.slime.services;

import cc.davyy.slime.model.SlimePlayer;
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

}