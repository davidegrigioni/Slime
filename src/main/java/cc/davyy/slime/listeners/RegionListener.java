package cc.davyy.slime.listeners;

import cc.davyy.slime.managers.RegionManager;
import com.google.inject.Inject;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockBreakEvent;

import static cc.davyy.slime.utils.FileUtils.getConfig;
import static cc.davyy.slime.utils.ColorUtils.of;
import static net.minestom.server.MinecraftServer.LOGGER;

public class RegionListener {

    private final RegionManager regionManager;

    @Inject
    public RegionListener(RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    public void init(GlobalEventHandler handler) {
        handler.addListener(PlayerBlockBreakEvent.class, event -> {
            final boolean blockBreakEnabled = getConfig().getBoolean("protection.disable-build-protection");
            final boolean messageEnabled = getConfig().getBoolean("protection.block-break-message.enable");

            if (blockBreakEnabled) {
                event.setCancelled(true);

                if (messageEnabled) {
                    final String message = getConfig().getString("protection.block-break-message.message");

                    if (message != null && !message.isEmpty()) {
                        event.getPlayer().sendMessage(of(message).build());
                        return;
                    }

                    LOGGER.warn("Block break message is not configured properly.");
                }
            }
        });
    }

}