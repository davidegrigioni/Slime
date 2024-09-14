package cc.davyy.slime.utils;

import cc.davyy.slime.misc.TagConstants;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

public final class JoinUtils {

    private static final ItemStack COMPASS = ItemStack.of(Material.COMPASS)
            .withTag(TagConstants.ACTION_TAG, "serversl");
    private static final ItemStack LOBBY_SELECTOR = ItemStack.of(Material.NETHER_STAR)
            .withTag(TagConstants.ACTION_TAG, "lobbysl");

    private JoinUtils() {}

    public static void applyJoinKit(@NotNull Player player) {
        final PlayerInventory inventory = player.getInventory();

        inventory.setItemStack(4, COMPASS);
        inventory.setItemStack(8, LOBBY_SELECTOR);
    }

}