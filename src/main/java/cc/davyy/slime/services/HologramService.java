package cc.davyy.slime.services;

import cc.davyy.slime.entities.holograms.HologramEntity;
import cc.davyy.slime.model.SlimePlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Service interface for managing holograms in the game.
 */
public interface HologramService {

    /**
     * Creates a hologram at the player's current position with the specified text.
     * <p>
     * This method will create a hologram and place it directly at the location where
     * the given player is currently standing, displaying the provided text.
     * </p>
     *
     * @param player the player who is creating the hologram
     * @param text the text to be displayed in the hologram
     */
    void createHologram(@NotNull SlimePlayer player, @NotNull Component text);

    /**
     * Moves an existing hologram to the current position of the specified player.
     * <p>
     * This method will relocate the hologram identified by the given ID to the position where
     * the specified player is currently standing.
     * </p>
     *
     * @param id the ID of the hologram to move
     * @param player the player whose current location will be the new location of the hologram
     */
    void moveHologram(int id, @NotNull SlimePlayer player);

    /**
     * Deletes the hologram with the specified ID and notifies the given player.
     * <p>
     * This method will remove the hologram identified by the given ID from the game world
     * and will send a notification to the specified player about the deletion.
     * </p>
     *
     * @param id the ID of the hologram to delete
     * @param player the player to notify about the hologram deletion
     */
    void deleteHologram(int id, @NotNull SlimePlayer player);

    /**
     * Retrieves the hologram entity associated with the given ID.
     * <p>
     * This method will return the hologram entity that corresponds to the specified ID.
     * If no hologram exists with the provided ID, it will return {@code null}.
     * </p>
     *
     * @param id the ID of the hologram to retrieve
     * @return the hologram entity associated with the ID, or {@code null} if not found
     */
    @NotNull
    HologramEntity getHologramById(int id);

    /**
     * Retrieves all hologram entities currently managed by the service.
     * <p>
     * This method returns a map of all hologram entities, where the key is the hologram's ID
     * and the value is the hologram entity itself.
     * </p>
     *
     * @return a map of all hologram entities
     */
    @NotNull
    Map<Integer, HologramEntity> getAllHologramEntities();

    /**
     * Clears all holograms managed by the service.
     * <p>
     * This method removes all holograms from the game world and resets the internal state
     * of the service to reflect that no holograms are currently present.
     * </p>
     */
    void clearHolograms();

}