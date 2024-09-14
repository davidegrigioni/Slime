package cc.davyy.slime.interfaces;

import cc.davyy.slime.entities.npc.NPCEntity;
import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.entity.PlayerSkin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface INPC {

    /**
     * Creates a new NPC and assigns it a unique ID.
     *
     * @param name   the name of the NPC
     * @param skin   the skin for the NPC
     * @param player the player for reference (for instance and position)
     */
    default void createNPC(@NotNull String name, @NotNull PlayerSkin skin, @NotNull SlimePlayer player) {}

    /**
     * Moves an NPC to the player's position if it exists.
     *
     * @param id       the ID of the NPC to move
     * @param player   the player to move the NPC to
     */
    void moveNPC(int id, @NotNull SlimePlayer player);

    /**
     * Deletes an NPC by its ID if it exists.
     *
     * @param id     the ID of the NPC to delete
     * @param player the player to send feedback to
     */
    void deleteNPC(int id, @NotNull SlimePlayer player);

    /**
     * Retrieves an NPC by its ID.
     *
     * @param id The NPC's ID.
     * @return The NPC entity, or null if not found.
     */
    NPCEntity getNPCById(int id);

    /**
     * Retrieves all NPC entities.
     *
     * @return A map of all NPC entities.
     */
    Map<Integer, NPCEntity> getAllNPCEntities();

    /**
     * Clears all NPCs from the instance and removes them from the map.
     */
    void clearNPCs();

}