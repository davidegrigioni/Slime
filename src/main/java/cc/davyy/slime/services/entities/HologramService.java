package cc.davyy.slime.services.entities;

import cc.davyy.slime.entities.HologramEntity;
import cc.davyy.slime.model.SlimePlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    Optional<HologramEntity> getHologramById(int id);

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

    /**
     * Adds a new line of text to the hologram with the specified ID.
     * <p>
     * This method appends a new line of text to the hologram identified by the given ID.
     * Each new line will appear below the existing lines.
     * </p>
     *
     * @param id the ID of the hologram to add a line to
     * @param text the text to add as a new line
     */
    void addHologramLine(@NotNull SlimePlayer player, int id, @NotNull Component text);

    /**
     * Inserts a line of text at a specific position in the hologram with the given ID.
     * <p>
     * This method inserts a line of text at the specified index in the hologram. Existing lines
     * will be shifted down to accommodate the new line.
     * </p>
     *
     * @param id the ID of the hologram to insert the line into
     * @param index the index where the new line will be inserted (0 for the top line)
     * @param text the text to insert as a line
     */
    void insertHologramLine(@NotNull SlimePlayer player, int id, int index, @NotNull Component text);

    /**
     * Removes a specific line of text from the hologram with the given ID.
     * <p>
     * This method removes the line of text at the specified index from the hologram.
     * The remaining lines will shift up to fill the gap.
     * </p>
     *
     * @param id the ID of the hologram to remove a line from
     * @param index the index of the line to remove (0 for the top line)
     */
    void removeHologramLine(@NotNull SlimePlayer player, int id, int index);

    /**
     * Replaces a specific line of text in the hologram with the specified ID.
     * <p>
     * This method updates the line of text at the given index in the hologram with new content.
     * </p>
     *
     * @param id the ID of the hologram to update a line in
     * @param index the index of the line to replace (0 for the top line)
     * @param newText the new text to display on the specified line
     */
    void updateHologramLine(@NotNull SlimePlayer player, int id, int index, @NotNull Component newText);

    /**
     * Retrieves all lines of text from the hologram with the specified ID.
     * <p>
     * This method returns a list of all text lines currently displayed by the hologram.
     * </p>
     *
     * @param id the ID of the hologram to retrieve lines from
     * @return a list of all lines of text in the hologram
     */
    @Nullable
    List<Component> getHologramLines(int id);

    void debug(@NotNull SlimePlayer player, int id);

    /**
     * Clears all lines of text from the hologram with the specified ID.
     * <p>
     * This method removes all lines of text from the hologram, leaving it empty.
     * </p>
     *
     * @param id the ID of the hologram to clear
     */
    void clearHologramLines(int id);


}