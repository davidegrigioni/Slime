package cc.davyy.slime.services.entities;

import cc.davyy.slime.entities.NPCEntity;
import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.entity.PlayerSkin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Service interface for managing NPCs (Non-Player Characters) in the game.
 */
public interface NPCService {

    /**
     * Creates a new NPC and assigns it a unique ID.
     * <p>
     * This method initializes a new NPC with the specified name and skin, placing it at the
     * position of the given player. The NPC will be assigned a unique ID upon creation.
     * </p>
     *
     * @param name   the name of the NPC.
     * @param skin   the skin for the NPC.
     * @param player the player used for reference, including the instance and position.
     */
    default void createNPC(@NotNull String name, @NotNull PlayerSkin skin, @NotNull SlimePlayer player) {}

    /**
     * Moves an NPC to the specified player's position if it exists.
     * <p>
     * This method will relocate the NPC identified by the given ID to the position of the provided
     * player. If the NPC does not exist, the operation will be ignored.
     * </p>
     *
     * @param id       the ID of the NPC to move.
     * @param player   the player whose position the NPC will be moved to.
     */
    void moveNPC(int id, @NotNull SlimePlayer player);

    /**
     * Deletes an NPC by its ID if it exists.
     * <p>
     * This method will remove the NPC identified by the given ID from the game world and send
     * feedback to the specified player. If the NPC does not exist, the operation will be ignored.
     * </p>
     *
     * @param id     the ID of the NPC to delete.
     * @param player the player to send feedback to about the deletion.
     */
    void deleteNPC(int id, @NotNull SlimePlayer player);

    /**
     * Retrieves an NPC by its ID.
     * <p>
     * This method returns the NPC entity associated with the specified ID. If no NPC with the
     * given ID exists, {@code null} will be returned.
     * </p>
     *
     * @param id The ID of the NPC to retrieve.
     * @return The {@link NPCEntity} associated with the given ID, or {@code null} if not found.
     */
    NPCEntity getNPCById(int id);

    /**
     * Retrieves all NPC entities.
     * <p>
     * This method returns a map of all NPC entities, where the keys are NPC IDs and the values
     * are the corresponding {@link NPCEntity} objects.
     * </p>
     *
     * @return A {@link Map} of all NPC entities, with NPC IDs as keys and {@link NPCEntity} objects as values.
     */
    @NotNull Map<Integer, NPCEntity> getAllNPCEntities();

    /**
     * Clears all NPCs from the instance and removes them from the map.
     * <p>
     * This method will remove all NPCs from the current instance, effectively clearing the
     * NPC map and removing their presence from the game world.
     * </p>
     */
    void clearNPCs();

}