package cc.davyy.slime.utils;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public final class InventoryConstants {

    public static final ItemStack SKYWARS_ITEM = ItemStack.of(Material.COMPASS)
            .withTag(TagConstants.SERVER_SWITCH_TAG, "skywars-server");
    public static final ItemStack BORDERLANDS_ITEM = ItemStack.of(Material.STICK);

}