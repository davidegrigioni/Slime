package cc.davyy.slime.listeners;

import cc.davyy.slime.managers.RegionManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

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
        final String regionName = regionManager.isPlayerInRegion(player.getPosition());
        final Title title = Title.title(Component.text("You're in"), Component.text(regionName));

        Check.notNull(regionName, "Region Name cannot be null");
        player.showTitle(title);

        return Result.SUCCESS;
    }

}