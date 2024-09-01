package cc.davyy.slime.listeners;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.ping.ResponseData;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.FileUtils.getConfig;

public class MOTDListener implements EventListener<ServerListPingEvent> {

    @Override
    public @NotNull Class<ServerListPingEvent> eventType() { return ServerListPingEvent.class; }

    @Override
    public @NotNull Result run(@NotNull ServerListPingEvent event) {
        final var onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers();
        final ResponseData responseData = event.getResponseData();

        final String description = getConfig().getString("motd.description");
        final boolean playersHidden = getConfig().getBoolean("motd.players-hidden");

        responseData.setOnline(onlinePlayers.size());
        responseData.setDescription(of(description)
                .build());
        responseData.setPlayersHidden(playersHidden);

        return Result.SUCCESS;
    }

}