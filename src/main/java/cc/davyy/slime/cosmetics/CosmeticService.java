package cc.davyy.slime.cosmetics;

import cc.davyy.slime.cosmetics.model.Cosmetic;
import cc.davyy.slime.model.SlimePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing cosmetics in the game.
 *
 * @param <T> The type of data associated with the cosmetic (e.g., ItemStack, ParticleEffect).
 */
public interface CosmeticService<T> {

    /**
     * Creates a new cosmetic item with the given name and data, and assigns it a type.
     * <p>
     * This method registers a new cosmetic of the specified type. The cosmetic can be applied to players
     * or used in various ways depending on its type.
     * </p>
     *
     * @param name the name of the cosmetic.
     * @param data the data associated with the cosmetic (e.g., item details, particle effect data).
     * @param type the type of the cosmetic (e.g., item, particle, mob).
     */
    void createCosmetic(@NotNull String name,
                        @NotNull T data,
                        @NotNull Cosmetic.CosmeticType type);

    /**
     * Removes the cosmetic with the specified ID.
     * <p>
     * This method deletes the cosmetic from the system based on its ID. It should be called if the cosmetic
     * is no longer needed or should be removed.
     * </p>
     *
     * @param id the ID of the cosmetic to remove.
     */
    void removeCosmetic(int id);

    /**
     * Retrieves a cosmetic by its ID.
     * <p>
     * This method returns an {@link Optional} containing the cosmetic if found, or an empty {@link Optional}
     * if the cosmetic does not exist.
     * </p>
     *
     * @param id the ID of the cosmetic to retrieve.
     * @return an {@link Optional} containing the cosmetic if found, or an empty {@link Optional} if not.
     */
    @NotNull
    Optional<Cosmetic<T>> getCosmeticByID(int id);

    /**
     * Applies the cosmetic with the specified ID to the given player.
     * <p>
     * This method applies the cosmetic to the player, making it visible or active for them. The player must be
     * valid and online for the cosmetic to be applied.
     * </p>
     *
     * @param player the player to whom the cosmetic will be applied.
     * @param id     the ID of the cosmetic to apply.
     */
    void applyCosmetic(@NotNull SlimePlayer player, int id);

    /**
     * Removes the cosmetic with the specified ID from the given player.
     * <p>
     * This method removes the cosmetic from the player, making it no longer visible or active for them.
     * </p>
     *
     * @param player the player from whom the cosmetic will be removed.
     * @param id     the ID of the cosmetic to remove.
     */
    void removeCosmetic(@NotNull SlimePlayer player, int id);

    /**
     * Gets a list of all available cosmetics.
     * <p>
     * This method returns a list of all cosmetics that are available for use in the game.
     * </p>
     *
     * @return a list of all available cosmetics.
     */
    @NotNull
    List<Cosmetic<T>> getAvailableCosmetics();

}