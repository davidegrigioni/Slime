package cc.davyy.slime;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.network.packet.client.ClientPacket;

public class PacketLogger {

    private final ComponentLogger LOGGER = ComponentLogger.logger(PacketLogger.class);

    public PacketLogger() {
        // Create a global event node to log packets
        EventNode<PlayerEvent> packetEventNode = EventNode.type("packet-logger", EventFilter.PLAYER);

        // Listen to incoming packets (from client)
        packetEventNode.addListener(PlayerPacketEvent.class, event -> {
            logPacket(event.getPacket(), event.getPlayer(), true);
        });

        // Listen to outgoing packets (to client)
        packetEventNode.addListener(PlayerPacketEvent.class, event -> {
            logPacket(event.getPacket(), event.getPlayer(), false);
        });

        // Register the event node to the global event handler
        MinecraftServer.getGlobalEventHandler().addChild(packetEventNode);
    }

    private void logPacket(ClientPacket packet, Player player, boolean isIncoming) {
        String direction = isIncoming ? "Incoming" : "Outgoing";
        LOGGER.info("{} packet from {}: {}", direction, player.getUsername(), packet.getClass().getSimpleName());

        // Optional: Log more detailed packet content if necessary
        LOGGER.info("Packet content: {}", packet);
    }

}