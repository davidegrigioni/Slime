package cc.davyy.slime.listeners;

import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Singleton;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PlayerEntityInteractListener implements EventListener<PlayerEntityInteractEvent> {

    @Override
    public @NotNull Class<PlayerEntityInteractEvent> eventType() {
        return PlayerEntityInteractEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerEntityInteractEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();
        final Entity target = event.getTarget();

        if (event.getHand() != Player.Hand.MAIN) return Result.INVALID;

        target.addPassenger(player);

        return Result.SUCCESS;
    }

}