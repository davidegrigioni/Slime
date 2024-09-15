package cc.davyy.slime.services;

import cc.davyy.slime.entities.holograms.HologramEntity;
import cc.davyy.slime.model.SlimePlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface HologramService {

    /**
     * Creates a hologram at the player's position with the given text.
     *
     * @param player the player creating the hologram
     * @param text   the text to display in the hologram
     */
    void createHologram(@NotNull SlimePlayer player, @NotNull Component text);

    /**
     * Moves the hologram with the given ID to the player's position.
     *
     * @param id     the ID of the hologram to move
     * @param player the player to move the hologram to
     */
    void moveHologram(int id, @NotNull SlimePlayer player);

    /**
     * Deletes the hologram with the given ID.
     *
     * @param id     the ID of the hologram to delete
     * @param player the player to notify of the deletion
     */
    void deleteHologram(int id, @NotNull SlimePlayer player);

    HologramEntity getHologramById(int id);

    Map<Integer, HologramEntity> getAllHologramEntities();

    void clearHolograms();

}