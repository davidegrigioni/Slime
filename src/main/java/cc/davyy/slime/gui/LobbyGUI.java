package cc.davyy.slime.gui;

import cc.davyy.slime.managers.LobbyManager;
import cc.davyy.slime.model.Lobby;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.TagConstants;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@Singleton
public class LobbyGUI extends Inventory {

    private final LobbyManager lobbyManager;

    @Inject
    public LobbyGUI(LobbyManager lobbyManager) {
        super(InventoryType.CHEST_1_ROW, "Lobbies");
        this.lobbyManager = lobbyManager;

        updateGUI();
    }

    public void open(@NotNull SlimePlayer player) {
        player.openInventory(this);
    }

    private void updateGUI() {
        this.clear();

        final Collection<Lobby> lobbies = lobbyManager.getAllLobbies();
        int slot = 0;

        for (Lobby lobby : lobbies) {
            if (slot >= this.getSize()) {
                break;
            }
            ItemStack item = createLobbyItem(lobby);
            this.setItemStack(slot++, item);
            listenToEvents();
        }

    }

    private void listenToEvents() {
        final var handler = MinecraftServer.getGlobalEventHandler();
        final var lobbyGUINode = EventNode.type("lobby-gui-listener", EventFilter.INVENTORY, ((inventoryEvent, inventory) -> this == inventory))
                .addListener(InventoryClickEvent.class, event -> {
                    final SlimePlayer player = (SlimePlayer) event.getPlayer();
                    final ItemStack clickedItem = event.getClickedItem();
                    final int clickedSlot = event.getSlot();
                    final Collection<Lobby> lobbies = lobbyManager.getAllLobbies();
                    final int lobbyTag = clickedItem.getTag(TagConstants.LOBBY_ID_TAG);

                    for (Lobby ignored : lobbies) {
                        if (clickedSlot == lobbyTag) {
                            lobbyManager.teleportPlayerToLobby(player, lobbyTag);
                        }
                    }

                });
        handler.addChild(lobbyGUINode);
    }

    private ItemStack createLobbyItem(@NotNull Lobby lobby) {
        return ItemStack.builder(Material.DIAMOND_BLOCK)
                .customName(Component.text(lobby.name()))
                .lore(Component.text("Players: " + lobby.sharedInstance().getPlayers().size()))
                .build();
    }

}