package cc.davyy.slime.managers.general;

import cc.davyy.slime.listeners.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.MinestomAdventure;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.event.trait.PlayerEvent;

@Singleton
public class EventsManager {

    private final InventoryPreClickListener inventoryPreClickListener;
    private final PlayerEntityInteractListener playerEntityInteractListener;
    private final PlayerPacketListener playerPacketListener;
    private final PlayerChatListener playerChatListener;
    private final PlayerMoveListener playerMoveListener;
    private final PlayerDisconnectListener playerDisconnectListener;
    private final PlayerJoinListener playerJoinListener;
    private final AsyncConfigListener asyncConfigListener;
    private final PlayerItemUseListener playerItemUseListener;
    private final PlayerBlockBreakListener playerBlockBreakListener;

    private EventNode<PlayerEvent> playerNode;

    @Inject
    public EventsManager(InventoryPreClickListener inventoryPreClickListener, PlayerEntityInteractListener playerEntityInteractListener, PlayerPacketListener playerPacketListener, PlayerChatListener playerChatListener, PlayerMoveListener playerMoveListener, PlayerDisconnectListener playerDisconnectListener, PlayerJoinListener playerJoinListener, AsyncConfigListener asyncConfigListener, PlayerItemUseListener playerItemUseListener, PlayerBlockBreakListener playerBlockBreakListener) {
        this.inventoryPreClickListener = inventoryPreClickListener;
        this.playerEntityInteractListener = playerEntityInteractListener;
        this.playerPacketListener = playerPacketListener;
        this.playerChatListener = playerChatListener;
        this.playerMoveListener = playerMoveListener;
        this.playerDisconnectListener = playerDisconnectListener;
        this.playerJoinListener = playerJoinListener;
        this.asyncConfigListener = asyncConfigListener;
        this.playerItemUseListener = playerItemUseListener;
        this.playerBlockBreakListener = playerBlockBreakListener;
    }

    public void init() {
        this.playerNode = createPlayerNode();
        MinecraftServer.getGlobalEventHandler().addChild(playerNode);

        MinestomAdventure.AUTOMATIC_COMPONENT_TRANSLATION = true;
        MinestomAdventure.COMPONENT_TRANSLATOR = (c, l) -> c;
    }

    private EventNode<PlayerEvent> createPlayerNode() {
        return EventNode.type("player-node", EventFilter.PLAYER)
                .addListener(inventoryPreClickListener)
                .addListener(playerEntityInteractListener)
                .addListener(playerPacketListener)
                .addListener(playerChatListener)
                .addListener(playerMoveListener)
                .addListener(playerDisconnectListener)
                .addListener(playerJoinListener)
                .addListener(asyncConfigListener)
                .addListener(playerItemUseListener)
                .addListener(ItemDropEvent.class, event -> event.setCancelled(true))
                .addListener(PlayerSwapItemEvent.class, event -> event.setCancelled(true))
                .addListener(playerBlockBreakListener);
    }

}