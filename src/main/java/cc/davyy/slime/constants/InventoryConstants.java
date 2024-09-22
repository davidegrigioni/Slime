package cc.davyy.slime.constants;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public final class InventoryConstants {

    public static final ItemStack SKYWARS_ITEM = ItemStack.builder(Material.GOLD_BLOCK)
            .set(TagConstants.SERVER_SWITCH_TAG, "skywars-server")
            .build();
    public static final ItemStack BORDERLANDS_ITEM = ItemStack.builder(Material.STICK)
            .build();

}