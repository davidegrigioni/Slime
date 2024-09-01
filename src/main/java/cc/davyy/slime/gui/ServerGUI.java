package cc.davyy.slime.gui;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.HeadProfile;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cc.davyy.slime.utils.ColorUtils.txtLore;
import static cc.davyy.slime.utils.FileUtils.getConfig;
import static cc.davyy.slime.utils.ColorUtils.of;

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
        final String navigatorLore = getConfig().getString("compass-item.lore");
        final ItemStack compass = createItem(Material.COMPASS, navigatorName, navigatorLore);
        inventory.setItemStack(NAVIGATOR_SLOT, compass);

        // Server settings
        final String settingsName = getConfig().getString("settings-item.name");
        final String settingsLore = getConfig().getString("settings-item.lore");
        final ItemStack settings = createItem(Material.REDSTONE, settingsName, settingsLore);
        inventory.setItemStack(SETTINGS_SLOT, settings);

        // Player management
        final String playersManagementName =  getConfig().getString("player-item.name");
        final String playersManagementLore = getConfig().getString("player-item.lore");
        final ItemStack playerManagement = ItemStack.builder(Material.PLAYER_HEAD)
                .customName(of(playersManagementName)
                        .build())
                .lore(txtLore(playersManagementLore))
                .set(ItemComponent.PROFILE, new HeadProfile(Objects.requireNonNull(player.getSkin())))
                .build();
        inventory.setItemStack(PLAYER_MANAGEMENT_SLOT, playerManagement);
    }

    private ItemStack createItem(@NotNull Material material, @NotNull String name, @NotNull String lore) {
        return ItemStack.builder(material)
                .customName(of(name)
                        .build())
                .lore(txtLore(lore))
                .build();
    }

}