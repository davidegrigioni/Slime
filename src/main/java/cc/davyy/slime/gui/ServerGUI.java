package cc.davyy.slime.gui;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.constants.InventoryConstants;
import cc.davyy.slime.constants.TagConstants;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cc.davyy.slime.utils.ColorUtils.stringListToComponentList;
import static cc.davyy.slime.utils.GeneralUtils.createItem;
import static cc.davyy.slime.utils.FileUtils.getConfig;
import static cc.davyy.slime.utils.ColorUtils.of;

@Singleton
public class ServerGUI extends Inventory {

    private static final int NAVIGATOR_SLOT = 13;
    private static final int SETTINGS_SLOT = 5;
    private static final int PLAYER_MANAGEMENT_SLOT = 11;

    private static final String GUI_TITLE = getConfig().getString("server-gui-title");

    public ServerGUI() {
        super(InventoryType.CHEST_3_ROW, of(GUI_TITLE).build());
        setupItems();
        listenToEvents();
    }

    public void open(@NotNull Player player) {
        player.openInventory(this);
    }

    private void setupItems() {
        // Navigation
        final String navigatorName = getConfig().getString("compass-item.name");
        final List<String> navigatorLore = getConfig().getStringList("compass-item.lore");
        final ItemStack compass = createItem(Material.COMPASS, navigatorName, navigatorLore);

        this.setItemStack(NAVIGATOR_SLOT, compass);
        this.setItemStack(16, InventoryConstants.SKYWARS_ITEM);

        // Server settings
        final String settingsName = getConfig().getString("settings-item.name");
        final List<String> settingsLore = getConfig().getStringList("settings-item.lore");
        final ItemStack settings = createItem(Material.REDSTONE, settingsName, settingsLore);
        //this.setItemStack(SETTINGS_SLOT, settings);

        // Player management
        final String playersManagementName =  getConfig().getString("player-item.name");
        final List<String> playersManagementLore = getConfig().getStringList("player-item.lore");
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
                .addListener(InventoryClickEvent.class, event -> {
                    final SlimePlayer player = (SlimePlayer) event.getPlayer();
                    final ItemStack item = event.getClickedItem();
                    final String skywarsServer = getConfig().getString("server.skywars");

                    switch (item.getTag(TagConstants.SERVER_SWITCH_TAG)) {
                        case "skywars-server" -> {
                            sendToServer(player, skywarsServer);
                        }
                    }
                });
        MinecraftServer.getGlobalEventHandler().addChild(serverNode);
    }

    private void sendToServer(@NotNull SlimePlayer player, @NotNull String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage("BungeeCord", out.toByteArray());
    }

}