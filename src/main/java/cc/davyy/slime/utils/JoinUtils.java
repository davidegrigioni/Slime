package cc.davyy.slime.utils;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

public final class JoinUtils {

    private static final ItemStack COMPASS = ItemStack.of(Material.COMPASS);

    private JoinUtils() {}

    public static void applyJoinKit(@NotNull Player player) {
        final PlayerInventory inventory = player.getInventory();

        inventory.setItemStack(4, COMPASS);
    }

}