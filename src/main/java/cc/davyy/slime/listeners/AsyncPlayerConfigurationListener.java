package cc.davyy.slime.listeners;

import cc.davyy.slime.managers.LobbyManager;
import cc.davyy.slime.utils.PosUtils;
import com.google.inject.Inject;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.utils.validate.Check;

import static cc.davyy.slime.utils.FileUtils.getConfig;

public class AsyncPlayerConfigurationListener {

    private final LobbyManager lobbyManager;

    @Inject
    public AsyncPlayerConfigurationListener(LobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
    }

    public void init(GlobalEventHandler handler) {
        handler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            final String posString = getConfig().getString("spawn.position");
            final Pos pos = PosUtils.fromString(posString);

            event.setSpawningInstance(lobbyManager.getMainLobbyContainer());

            Check.notNull(pos, "Position cannot be null, Check your Config!");
            player.setRespawnPoint(pos);
        });
    }

}