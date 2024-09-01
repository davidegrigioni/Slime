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

    private final Inventory inventory = new Inventory(InventoryType.CHEST_3_ROW, "Test");

    public void setupGui(@NotNull Player player) {
        inventory.addInventoryCondition(((players, slot, clickType, result) -> result.setCancel(true)));

        setupItems(player);
    }

    private void setupItems(@NotNull Player player) {
        // Navigation
        final ItemStack compass = ItemStack.builder(Material.COMPASS)
                .customName(of(getConfig().getString("compass-item.name"))
                        .build())
                .lore(txtLore(getConfig().getString("compass-item.lore")))
                .build();
        inventory.setItemStack(3, compass);

        // Server settings
        final ItemStack settings = ItemStack.builder(Material.REDSTONE)
                .customName(of(getConfig().getString("settings-item.name"))
                        .build())
                .lore(txtLore(getConfig().getString("settings-item.lore")))
                .build();
        inventory.setItemStack(5, settings);

        // Player management
        final ItemStack playerManagement = ItemStack.builder(Material.PLAYER_HEAD)
                .customName(of(getConfig().getString("player-item.name"))
                        .build())
                .lore(txtLore(getConfig().getString("player-item.lore")))
                .set(ItemComponent.PROFILE, new HeadProfile(Objects.requireNonNull(player.getSkin())))
                .build();
        inventory.setItemStack(11, playerManagement);
    }

}