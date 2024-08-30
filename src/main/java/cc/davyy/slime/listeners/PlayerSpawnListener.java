package cc.davyy.slime.listeners;

import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerSpawnEvent;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.JoinUtils.applyJoinKit;

public class PlayerSpawnListener implements EventListener<PlayerSpawnEvent> {

    @Override
    public @NotNull Class<PlayerSpawnEvent> eventType() { return PlayerSpawnEvent.class; }

    @Override
    public @NotNull Result run(@NotNull PlayerSpawnEvent event) {
        final Player player = event.getPlayer();

        applyJoinKit(player);

        return Result.SUCCESS;
    }

}