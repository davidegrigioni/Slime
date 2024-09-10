package cc.davyy.slime.listeners;

import cc.davyy.slime.managers.SidebarManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;

import static cc.davyy.slime.utils.FileUtils.getConfig;
import static cc.davyy.slime.utils.JoinUtils.applyJoinKit;
import static cc.davyy.slime.utils.ColorUtils.of;

public class PlayerSpawnListener {

    private final SidebarManager sidebarManager;

    @Inject
    public PlayerSpawnListener(SidebarManager sidebarManager) {
        this.sidebarManager = sidebarManager;
    }

    public void init(GlobalEventHandler handler) {
        handler.addListener(PlayerSpawnEvent.class, event -> {
            final SlimePlayer player = (SlimePlayer) event.getPlayer();
            final String header = getConfig().getString("header");
            final String footer = getConfig().getString("footer");

            player.sendPlayerListHeaderAndFooter(
                    of(header).build(),
                    of(footer).build()
            );

            sidebarManager.showSidebar(player);

            applyJoinKit(player);
        });
        handler.addListener(PlayerDisconnectEvent.class, event -> {
            final SlimePlayer player = (SlimePlayer) event.getPlayer();

            sidebarManager.removeSidebar(player);
        });
    }

}