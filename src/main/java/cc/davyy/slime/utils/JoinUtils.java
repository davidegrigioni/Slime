package cc.davyy.slime.utils;

import cc.davyy.slime.constants.TagConstants;
import cc.davyy.slime.managers.general.ConfigManager;
import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.minestom.server.MinecraftServer.LOGGER;

public final class JoinUtils {

    private JoinUtils() {}

    public static void applyJoinKit(@NotNull SlimePlayer player, @NotNull ConfigManager configManager) {
        final PlayerInventory inventory = player.getInventory();

        applyItemToSlot(inventory, "items.show-item", 2, configManager);
        applyItemToSlot(inventory, "items.lobby-item", 8, configManager);
        applyItemToSlot(inventory, "items.compass-item", 4, configManager);
    }

    public static ItemStack createItemFromConfig(@NotNull String configPath, @NotNull ConfigManager configManager) {
        final String materialName = configManager.getConfig().getString(configPath + ".material");
        final Material material = Material.fromNamespaceId(materialName);

        if (material == null) {
            throw new IllegalArgumentException("Invalid material: " + materialName);
        }

        final String displayName = configManager.getConfig().getString(configPath + ".display-name");
        final List<String> itemLore = configManager.getConfig().getStringList(configPath + ".lore");

        final String actionTag = configManager.getConfig().getString(configPath + ".action-tag");

        return ItemStack.builder(material)
                .customName(ColorUtils.of(displayName)
                        .build())
                .lore(ColorUtils.stringListToComponentList(itemLore))
                .set(TagConstants.ACTION_TAG, actionTag)
                .build();
    }

    private static void applyItemToSlot(@NotNull PlayerInventory inventory, @NotNull String configPath, int defaultSlot, @NotNull ConfigManager configManager) {
        final ItemStack item = createItemFromConfig(configPath, configManager);

        int slot = configManager.getConfig().getInt(configPath + ".slot");

        if (slot < 0 || slot > 8) {
            LOGGER.info("Invalid slot {} for {}, using default slot {}", slot, configPath, defaultSlot);
            slot = defaultSlot;
        }

        inventory.setItemStack(slot, item);
    }

}