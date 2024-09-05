package cc.davyy.slime.gui;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cc.davyy.slime.utils.ColorUtils.*;
import static cc.davyy.slime.utils.FileUtils.getConfig;

public class ServerGUI {

    private static final int NAVIGATOR_SLOT = 3;
    private static final int SETTINGS_SLOT = 5;
    private static final int PLAYER_MANAGEMENT_SLOT = 11;
    private static final String GUI_TITLE = getConfig().getString("server-gui-title");

    private final Inventory inventory = new Inventory(InventoryType.CHEST_3_ROW, of(GUI_TITLE)
            .build());

    public void open(@NotNull Player player) {
        setupGui(player);
        player.openInventory(inventory);
    }

    private void setupGui(@NotNull Player player) {
        inventory.addInventoryCondition(((players, slot, clickType, result) -> result.setCancel(true)));
        setupItems(player);
    }

    private void setupItems(@NotNull Player player) {
        // Navigation
        final String navigatorName = getConfig().getString("compass-item.name");
        final List<String> navigatorLore = getConfig().getStringList("compass-item.lore");
        final ItemStack compass = createItem(Material.COMPASS, navigatorName, navigatorLore);
        inventory.setItemStack(NAVIGATOR_SLOT, compass);

        // Server settings
        final String settingsName = getConfig().getString("settings-item.name");
        final List<String> settingsLore = getConfig().getStringList("settings-item.lore");
        final ItemStack settings = createItem(Material.REDSTONE, settingsName, settingsLore);
        inventory.setItemStack(SETTINGS_SLOT, settings);

        // Player management
        final String playersManagementName =  getConfig().getString("player-item.name");
        final List<String> playersManagementLore = getConfig().getStringList("player-item.lore");
        final ItemStack playerManagement = ItemStack.builder(Material.PLAYER_HEAD)
                .customName(of(playersManagementName)
                        .build())
                .lore(stringListToComponentList(playersManagementLore))
                //.set(ItemComponent.PROFILE, new HeadProfile(PlayerSkin.fromUsername("davideenoo")))
                .build();
        inventory.setItemStack(PLAYER_MANAGEMENT_SLOT, playerManagement);
    }

    private @NotNull ItemStack createItem(@NotNull Material material, @NotNull String name, @NotNull List<String> lore) {
        return ItemStack.builder(material)
                .customName(of(name)
                        .build())
                .lore(stringListToComponentList(lore))
                .build();
    }

}