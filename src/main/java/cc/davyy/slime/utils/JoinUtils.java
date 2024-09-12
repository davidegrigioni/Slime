package cc.davyy.slime.utils;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

public final class JoinUtils {

    private static final ItemStack COMPASS = ItemStack.of(Material.COMPASS);
    private static final ItemStack LOBBY_SELECTOR = ItemStack.of(Material.NETHER_STAR)
            .withTag(Tag.String("action"), "lobbysl");

    private JoinUtils() {}

    public static void applyJoinKit(@NotNull Player player) {
        final PlayerInventory inventory = player.getInventory();

        inventory.setItemStack(4, COMPASS);
        inventory.setItemStack(8, LOBBY_SELECTOR);
    }

}