package cc.davyy.slime.utils;

import cc.davyy.slime.constants.TagConstants;
import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.minestom.server.MinecraftServer.LOGGER;

public final class JoinUtils {

    private JoinUtils() {}

    public static void applyJoinKit(@NotNull SlimePlayer player) {
        final PlayerInventory inventory = player.getInventory();

        applyItemToSlot(inventory, "items.show-item", 2);
        applyItemToSlot(inventory, "items.lobby-item", 8);
        applyItemToSlot(inventory, "items.compass-item", 4);
    }

    public static ItemStack createItemFromConfig(@NotNull String configPath) {
        final String materialName = FileUtils.getConfig().getString(configPath + ".material");
        final Material material = Material.fromNamespaceId(materialName);

        if (material == null) {
            throw new IllegalArgumentException("Invalid material: " + materialName);
        }

        final String displayName = FileUtils.getConfig().getString(configPath + ".display-name");
        final List<String> itemLore = FileUtils.getConfig().getStringList(configPath + ".lore");

        final String actionTag = FileUtils.getConfig().getString(configPath + ".action-tag");

        return ItemStack.builder(material)
                .customName(ColorUtils.of(displayName)
                        .build())
                .lore(ColorUtils.stringListToComponentList(itemLore))
                .set(TagConstants.ACTION_TAG, actionTag)
                .build();
    }

    private static void applyItemToSlot(@NotNull PlayerInventory inventory, @NotNull String configPath, int defaultSlot) {
        final ItemStack item = createItemFromConfig(configPath);

        int slot = FileUtils.getConfig().getInt(configPath + ".slot");

        if (slot < 0 || slot > 8) {
            LOGGER.info("Invalid slot {} for {}, using default slot {}", slot, configPath, defaultSlot);
            slot = defaultSlot;
        }

        inventory.setItemStack(slot, item);
    }

}