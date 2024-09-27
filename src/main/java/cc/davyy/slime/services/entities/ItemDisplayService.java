package cc.davyy.slime.services.entities;

import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.entity.metadata.display.ItemDisplayMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.location.RelativeVec;
import org.jetbrains.annotations.NotNull;

public interface ItemDisplayService {

    /**
     * Summons an ItemDisplay entity and places it at the player's current position.
     *
     * @param player the player who is summoning the item display.
     */
    void summonItemDisplay(@NotNull SlimePlayer player);

    /**
     * Modifies the ItemDisplay entity's properties.
     *
     * @param player        the player who owns the item display.
     * @param itemStack     the item stack to display.
     * @param scale         the scale to apply to the item display.
     * @param displayContext the display context of the item.
     */
    void modifyItemDisplay(@NotNull SlimePlayer player,
                           @NotNull ItemStack itemStack,
                           @NotNull RelativeVec scale,
                           @NotNull ItemDisplayMeta.DisplayContext displayContext);

    /**
     * Deletes an ItemDisplay entity if it exists for the player.
     *
     * @param player the player whose item display will be removed.
     */
    void removeItemDisplay(@NotNull SlimePlayer player);

    /**
     * Clears all item displays for all players.
     */
    void clearAllDisplays();

}