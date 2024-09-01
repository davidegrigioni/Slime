package cc.davyy.slime.listeners;

import cc.davyy.slime.managers.RegionManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RegionListener implements EventListener<PlayerMoveEvent> {

    private final RegionManager regionManager;

    public RegionListener(RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @Override
    public @NotNull Class<PlayerMoveEvent> eventType() { return PlayerMoveEvent.class; }

    @Override
    public @NotNull Result run(@NotNull PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Optional<String> regionNameOpt = regionManager.isPlayerInRegion(player.getPosition());

        regionNameOpt.ifPresentOrElse(regionName -> {
            final Title title = Title.title(Component.text("You're in"), Component.text(regionName));
            player.showTitle(title);
        }, player::clearTitle);

        return Result.SUCCESS;
    }

}