package cc.davyy.slime.listeners;

import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.FileUtils.getConfig;

public class BBListener implements EventListener<PlayerBlockBreakEvent> {

    @Override
    public @NotNull Class<PlayerBlockBreakEvent> eventType() { return PlayerBlockBreakEvent.class; }

    @Override
    public @NotNull Result run(@NotNull PlayerBlockBreakEvent event) {
        final boolean blockBreakEnabled = getConfig().getBoolean("disable-build-protection");

        if (blockBreakEnabled) {
            event.setCancelled(true);
            return Result.SUCCESS;
        }

        return Result.SUCCESS;
    }

}