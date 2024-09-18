package cc.davyy.slime.services.cosmetics;

import cc.davyy.slime.model.cosmetics.Cosmetic;
import cc.davyy.slime.model.SlimePlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing cosmetics in the game.
 *
 */
public interface CosmeticService<T extends Cosmetic> {

    /**
     * Creates a cosmetic and registers it in the system.
     *
     * @param name The name of the cosmetic.
     * @param data The specific data related to the cosmetic (e.g., ItemStack or EntityType).
     */
    void createCosmetic(@NotNull Component name, @NotNull Object data);

    /**
     * Applies the cosmetic to the given player.
     *
     * @param player The player to apply the cosmetic to.
     * @param id The ID of the cosmetic to apply.
     */
    void applyCosmetic(@NotNull SlimePlayer player, int id);

    /**
     * Removes the cosmetic from the given player.
     *
     * @param player The player to remove the cosmetic from.
     * @param id The ID of the cosmetic to remove.
     */
    void removeCosmetic(@NotNull SlimePlayer player, int id);

    /**
     * Retrieves a cosmetic by its ID.
     *
     * @param id The ID of the cosmetic.
     * @return An optional containing the cosmetic if found.
     */
    @NotNull
    Optional<T> getCosmeticByID(int id);

    /**
     * Returns a list of all available cosmetics.
     *
     * @return A list of all available cosmetics.
     */
    @NotNull
    List<T> getAvailableCosmetics();

}