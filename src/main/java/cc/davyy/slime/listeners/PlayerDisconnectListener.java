package cc.davyy.slime.listeners;

import cc.davyy.slime.managers.entities.SidebarManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PlayerDisconnectListener implements EventListener<PlayerDisconnectEvent> {

    private final SidebarManager sidebarManager;

    @Inject
    public PlayerDisconnectListener(SidebarManager sidebarManager) {
        this.sidebarManager = sidebarManager;
    }

    @Override
    public @NotNull Class<PlayerDisconnectEvent> eventType() {
        return PlayerDisconnectEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerDisconnectEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();

        sidebarManager.removeSidebar(player);

        return Result.SUCCESS;
    }

}