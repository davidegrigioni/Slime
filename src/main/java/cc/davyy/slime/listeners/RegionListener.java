package cc.davyy.slime.listeners;

import cc.davyy.slime.managers.RegionManager;
import com.google.inject.Inject;
import net.kyori.adventure.title.Title;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerMoveEvent;

import java.util.Optional;

import static cc.davyy.slime.utils.FileUtils.getConfig;
import static cc.davyy.slime.utils.FileUtils.getMessages;
import static cc.davyy.slime.utils.ColorUtils.of;

public class RegionListener {

    private final RegionManager regionManager;

    @Inject
    public RegionListener(RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    public void init(GlobalEventHandler handler) {
        /*handler.addListener(PlayerMoveEvent.class, event -> {
            final Player player = event.getPlayer();
            final Optional<String> regionNameOpt = regionManager.isPlayerInRegion(player.getPosition());

            regionNameOpt.ifPresentOrElse(regionName -> {
                String mainTitle = getMessages().getString("titles.title");
                String subTitle = getMessages().getString("titles.subtitle");
                final Title title = Title.title(of(mainTitle)
                        .build(), of(subTitle)
                        .parseMMP("regionname", regionName)
                        .build());
                player.showTitle(title);
            }, player::clearTitle);
        });*/
        handler.addListener(PlayerBlockBreakEvent.class, event -> {
            final boolean blockBreakEnabled = getConfig().getBoolean("disable-build-protection");
            final boolean messageEnabled = getConfig().getBoolean("block-break-message.enable");

            if (blockBreakEnabled) {
                event.setCancelled(true);

                if (messageEnabled) {
                    final String message = getMessages().getString("block-break-message.message");
                    event.getPlayer().sendMessage(of(message)
                            .build());
                }
            }
        });
    }

}