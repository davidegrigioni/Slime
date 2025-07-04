package cc.davyy.slime.gui;

import cc.davyy.slime.config.ConfigManager;
import cc.davyy.slime.managers.gameplay.LobbyManager;
import cc.davyy.slime.model.Lobby;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.constants.TagConstants;
import cc.davyy.slime.services.gameplay.GUIService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static cc.davyy.slime.utils.ColorUtils.of;
import static net.kyori.adventure.text.Component.text;

@Singleton
public class LobbyGUI extends Inventory implements GUIService {

    private final String lobbyGuiTitle;
    private final LobbyManager lobbyManager;

    @Inject
    public LobbyGUI(ConfigManager configManager, @Named("lobbyGuiTitle") String lobbyGuiTitle, LobbyManager lobbyManager) {
        super(InventoryType.CHEST_1_ROW, of(lobbyGuiTitle).build());
        this.lobbyGuiTitle = lobbyGuiTitle;
        this.lobbyManager = lobbyManager;

        listen();
    }

    @Override
    public void open(@NotNull SlimePlayer player) {
        player.openInventory(this);
    }

    @Override
    public void updateGUI() {
        this.clear();

        final Collection<Lobby> lobbies = lobbyManager.getAllLobbies();
        int slot = 0;

        for (Lobby lobby : lobbies) {
            if (slot >= this.getSize()) {
                break;
            }
            ItemStack item = createLobbyItem(lobby);
            this.setItemStack(slot++, item);
        }
    }

    @Override
    public void listen() {
        final var lobbyNode = EventNode.type("lobby-node", EventFilter.INVENTORY, ((inventoryEvent, inventory) -> this == inventory))
                .addListener(InventoryPreClickEvent.class, event -> {
                    final SlimePlayer player = (SlimePlayer) event.getPlayer();
                    final ItemStack lobbyItem = event.getClickedItem();
                    final Integer lobbyTag = lobbyItem.getTag(TagConstants.LOBBY_ID_TAG);

                    event.setCancelled(true);

                    if (lobbyItem.hasTag(TagConstants.LOBBY_ID_TAG)) {
                        lobbyManager.teleportPlayerToLobby(player, lobbyTag);
                    }
                });
        MinecraftServer.getGlobalEventHandler().addChild(lobbyNode);
    }

    private ItemStack createLobbyItem(@NotNull Lobby lobby) {
        return ItemStack.builder(Material.DIAMOND_BLOCK)
                .set(TagConstants.LOBBY_ID_TAG, lobby.id())
                .customName(text(lobby.name()))
                .lore(text("Players: " + lobby.sharedInstance().getPlayers().size()))
                .build();
    }

}