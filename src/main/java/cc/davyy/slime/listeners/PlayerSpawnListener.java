package cc.davyy.slime.listeners;

import cc.davyy.slime.managers.SidebarManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerSpawnEvent;

import static cc.davyy.slime.utils.JoinUtils.applyJoinKit;

public class PlayerSpawnListener {

    private final SidebarManager sidebarManager;

    @Inject
    public PlayerSpawnListener(SidebarManager sidebarManager) {
        this.sidebarManager = sidebarManager;
    }

    public void init(GlobalEventHandler handler) {
        handler.addListener(PlayerSpawnEvent.class, event -> {
            final SlimePlayer player = (SlimePlayer) event.getPlayer();

            sidebarManager.showSidebar(player);

            applyJoinKit(player);
        });
    }

}