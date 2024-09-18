package cc.davyy.slime.gui;

import cc.davyy.slime.managers.LobbyManager;
import cc.davyy.slime.model.Lobby;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.constants.TagConstants;
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

import static cc.davyy.slime.utils.FileUtils.getConfig;
import static cc.davyy.slime.utils.ColorUtils.of;
import static net.minestom.server.MinecraftServer.LOGGER;

@Singleton
public class LobbyGUI extends Inventory {

    private final LobbyManager lobbyManager;

    private static final String LOBBY_GUI_TITLE = getConfig().getString("lobby-gui-title");

    @Inject
    public LobbyGUI(LobbyManager lobbyManager) {
        super(InventoryType.CHEST_1_ROW, of(LOBBY_GUI_TITLE).build());
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
        }
    }

    private ItemStack createLobbyItem(@NotNull Lobby lobby) {
        return ItemStack.builder(Material.DIAMOND_BLOCK)
                .set(TagConstants.LOBBY_ID_TAG, lobby.id())
                .customName(Component.text(lobby.name()))
                .lore(Component.text("Players: " + lobby.sharedInstance().getPlayers().size()))
                .build();
    }

}