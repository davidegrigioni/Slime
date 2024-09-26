package cc.davyy.slime.gui;

import cc.davyy.slime.managers.general.ConfigManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
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
import net.minestom.server.network.NetworkBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cc.davyy.slime.utils.ColorUtils.stringListToComponentList;
import static cc.davyy.slime.utils.GeneralUtils.createItem;
import static cc.davyy.slime.utils.ColorUtils.of;

@Singleton
public class ServerGUI extends Inventory {

    private static final int NAVIGATOR_SLOT = 13;
    private static final int SETTINGS_SLOT = 15;
    private static final int PLAYER_MANAGEMENT_SLOT = 11;

    private final ConfigManager configManager;
    private final String guiTitle;

    @Inject
    public ServerGUI(ConfigManager configManager, String guiTitle) {
        super(InventoryType.CHEST_3_ROW, of(guiTitle).build());
        this.configManager = configManager;
        this.guiTitle = configManager.getConfig().getString("server-gui-title");
        setupItems();
        listenToEvents();
    }

    public void open(@NotNull SlimePlayer player) {
        player.openInventory(this);
    }

    private void setupItems() {
        fillBackground();

        // Navigation
        final String navigatorName = configManager.getConfig().getString("compass-item.name");
        final List<String> navigatorLore = configManager.getConfig().getStringList("compass-item.lore");
        final ItemStack compass = createItem(Material.COMPASS, navigatorName, navigatorLore);
        this.setItemStack(NAVIGATOR_SLOT, compass);

        // Server settings
        final String settingsName = configManager.getConfig().getString("settings-item.name");
        final List<String> settingsLore = configManager.getConfig().getStringList("settings-item.lore");
        final ItemStack settings = createItem(Material.REDSTONE, settingsName, settingsLore);
        this.setItemStack(SETTINGS_SLOT, settings);

        // Player management
        final String playersManagementName = configManager.getConfig().getString("player-item.name");
        final List<String> playersManagementLore = configManager.getConfig().getStringList("player-item.lore");
        final ItemStack playerManagement = ItemStack.builder(Material.PLAYER_HEAD)
                .customName(of(playersManagementName).build())
                .lore(stringListToComponentList(playersManagementLore))
                //.set(ItemComponent.PROFILE, new HeadProfile(PlayerSkin.fromUsername("davideenoo"))) // Uncomment if you have player skins
                .build();
        this.setItemStack(PLAYER_MANAGEMENT_SLOT, playerManagement);
    }

    private void listenToEvents() {
        var serverNode = EventNode.type("server-inv", EventFilter.INVENTORY, ((inventoryEvent, inventory) -> this == inventory))
                .addListener(InventoryPreClickEvent.class, event -> {
                    final SlimePlayer player = (SlimePlayer) event.getPlayer();
                    final ItemStack item = event.getClickedItem();

                    final String commandToExecute = configManager.getConfig().getString("item-commands." + item.material().name());

                    if (commandToExecute != null && !commandToExecute.isEmpty()) {
                        MinecraftServer.getCommandManager().execute(player, commandToExecute);

                        player.sendMessage(Component.text("Command executed: " + commandToExecute)
                                .color(NamedTextColor.GREEN));
                    }

                    event.setCancelled(true);
                });
        MinecraftServer.getGlobalEventHandler().addChild(serverNode);
    }

    private void fillBackground() {
        final ItemStack glassPane = ItemStack.builder(Material.GLASS_PANE)
                .customName(Component.text(" "))
                .build();

        for (int i = 0; i < this.getSize(); i++) {
            if (i != NAVIGATOR_SLOT && i != SETTINGS_SLOT && i != PLAYER_MANAGEMENT_SLOT) {
                this.setItemStack(i, glassPane);
            }
        }
    }

    private void sendToServer(@NotNull SlimePlayer player, @NotNull String server) {
        player.sendPluginMessage("BungeeCord", NetworkBuffer.makeArray(buffer -> {
            buffer.write(NetworkBuffer.STRING, "Connect");
            buffer.write(NetworkBuffer.STRING, server);
        }));
    }

}