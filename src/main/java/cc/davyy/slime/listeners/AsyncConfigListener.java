package cc.davyy.slime.listeners;

import cc.davyy.slime.managers.ConfigManager;
import cc.davyy.slime.managers.LobbyManager;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.PosUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

@Singleton
public class AsyncConfigListener implements EventListener<AsyncPlayerConfigurationEvent> {

    private final ConfigManager configManager;
    private final LobbyManager lobbyManager;

    @Inject
    public AsyncConfigListener(ConfigManager configManager, LobbyManager lobbyManager) {
        this.configManager = configManager;
        this.lobbyManager = lobbyManager;
    }

    @Override
    public @NotNull Class<AsyncPlayerConfigurationEvent> eventType() {
        return AsyncPlayerConfigurationEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull AsyncPlayerConfigurationEvent event) {
        final SlimePlayer player = (cc.davyy.slime.model.SlimePlayer) event.getPlayer();
        final String posString = configManager.getConfig().getString("spawn.position");
        final Pos pos = PosUtils.fromString(posString);
        final Instance instance = lobbyManager.getMainLobbyContainer();

        if (!player.hasLobbyID()) {
            final int lobbyIDForInstance = lobbyManager.getLobbyIDForInstance(instance);
            player.setLobbyID(lobbyIDForInstance);
            event.setSpawningInstance(instance);
            return Result.INVALID;
        }

        Check.notNull(pos, "Position cannot be null, Check your Config!");
        player.setRespawnPoint(pos);

        return Result.SUCCESS;
    }

}