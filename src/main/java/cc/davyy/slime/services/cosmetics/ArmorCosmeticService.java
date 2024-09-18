package cc.davyy.slime.services.cosmetics;

import cc.davyy.slime.model.cosmetics.ArmorCosmetic;
import cc.davyy.slime.model.cosmetics.ArmorData;
import cc.davyy.slime.model.SlimePlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface ArmorCosmeticService {

    void createCosmetic(@NotNull Component name,
                        @NotNull ArmorData armorData);

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
    Optional<ArmorCosmetic> getCosmeticByID(int id);

    /**
     * Returns a list of all available cosmetics.
     *
     * @return A list of all available cosmetics.
     */
    @NotNull
    List<ArmorCosmetic> getAvailableCosmetics();

}