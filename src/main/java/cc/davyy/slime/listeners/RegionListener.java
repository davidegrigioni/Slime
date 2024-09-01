package cc.davyy.slime.listeners;

import cc.davyy.slime.managers.RegionManager;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerMoveEvent;

import java.util.Optional;

import static cc.davyy.slime.utils.FileUtils.getConfig;

public class RegionListener {

    private final RegionManager regionManager;

    @Inject
    public RegionListener(RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    public void init(GlobalEventHandler handler) {
        handler.addListener(PlayerMoveEvent.class, event -> {
            final Player player = event.getPlayer();
            final Optional<String> regionNameOpt = regionManager.isPlayerInRegion(player.getPosition());

            regionNameOpt.ifPresentOrElse(regionName -> {
                final Title title = Title.title(Component.text("You're in"), Component.text(regionName));
                player.showTitle(title);
            }, player::clearTitle);
        });
        handler.addListener(PlayerBlockBreakEvent.class, event -> {
            final boolean blockBreakEnabled = getConfig().getBoolean("disable-build-protection");

            if (blockBreakEnabled) { event.setCancelled(true); }
        });
    }

}