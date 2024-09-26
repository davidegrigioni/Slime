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

    private EventNode<PlayerEvent> playerNode;

    @Inject private InventoryPreClickListener inventoryPreClickListener;
    @Inject private PlayerEntityInteractListener playerEntityInteractListener;
    @Inject private PlayerPacketListener playerPacketListener;
    @Inject private PlayerChatListener playerChatListener;
    @Inject private PlayerMoveListener playerMoveListener;
    @Inject private PlayerDisconnectListener playerDisconnectListener;
    @Inject private PlayerJoinListener playerJoinListener;
    @Inject private AsyncConfigListener asyncConfigListener;
    @Inject private PlayerItemUseListener playerItemUseListener;
    @Inject private PlayerBlockBreakListener playerBlockBreakListener;

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