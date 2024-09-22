package cc.davyy.slime.gui;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.constants.InventoryConstants;
import cc.davyy.slime.utils.FileUtils;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cc.davyy.slime.utils.ColorUtils.stringListToComponentList;
import static cc.davyy.slime.utils.GeneralUtils.createItem;
import static cc.davyy.slime.utils.ColorUtils.of;

@Singleton
public class ServerGUI extends Inventory {

    private static final int NAVIGATOR_SLOT = 13;
    private static final int SETTINGS_SLOT = 5;
    private static final int PLAYER_MANAGEMENT_SLOT = 11;

    private static final String GUI_TITLE = FileUtils.getConfig().getString("server-gui-title");

    public ServerGUI() {
        super(InventoryType.CHEST_3_ROW, of(GUI_TITLE).build());
        setupItems();
        listenToEvents();
    }

    public void open(@NotNull SlimePlayer player) {
        player.openInventory(this);
    }

    private void setupItems() {
        // Navigation
        final String navigatorName = FileUtils.getConfig().getString("compass-item.name");
        final List<String> navigatorLore = FileUtils.getConfig().getStringList("compass-item.lore");
        final ItemStack compass = createItem(Material.COMPASS, navigatorName, navigatorLore);

        this.setItemStack(NAVIGATOR_SLOT, compass);
        this.setItemStack(16, InventoryConstants.SKYWARS_ITEM);

        // Server settings
        final String settingsName = FileUtils.getConfig().getString("settings-item.name");
        final List<String> settingsLore = FileUtils.getConfig().getStringList("settings-item.lore");
        final ItemStack settings = createItem(Material.REDSTONE, settingsName, settingsLore);
        //this.setItemStack(SETTINGS_SLOT, settings);

        // Player management
        final String playersManagementName = FileUtils.getConfig().getString("player-item.name");
        final List<String> playersManagementLore = FileUtils.getConfig().getStringList("player-item.lore");
        final ItemStack playerManagement = ItemStack.builder(Material.PLAYER_HEAD)
                .customName(of(playersManagementName)
                        .build())
                .lore(stringListToComponentList(playersManagementLore))
                //.set(ItemComponent.PROFILE, new HeadProfile(PlayerSkin.fromUsername("davideenoo")))
                .build();
        //this.setItemStack(PLAYER_MANAGEMENT_SLOT, playerManagement);
    }

    private void listenToEvents() {
        var serverNode = EventNode.type("server-inv", EventFilter.INVENTORY, ((inventoryEvent, inventory) -> this == inventory))
                .addListener(InventoryPreClickEvent.class, event -> {
                    final SlimePlayer player = (SlimePlayer) event.getPlayer();
                    final ItemStack item = event.getClickedItem();

                    final String commandToExecute = FileUtils.getConfig().getString("item-commands." + item.material().name());

                    if (commandToExecute != null && !commandToExecute.isEmpty()) {
                        MinecraftServer.getCommandManager().execute(player, commandToExecute);

                        player.sendMessage(Component.text("Command executed: " + commandToExecute)
                                .color(NamedTextColor.GREEN));
                    }

                    event.setCancelled(true);
                });
        MinecraftServer.getGlobalEventHandler().addChild(serverNode);
    }

}