package cc.davyy.slime.listeners;

import cc.davyy.slime.managers.ConfigManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.ColorUtils.of;
import static net.minestom.server.MinecraftServer.LOGGER;

@Singleton
public class PlayerBlockBreakListener implements EventListener<PlayerBlockBreakEvent> {

    private final ConfigManager configManager;

    @Inject
    public PlayerBlockBreakListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public @NotNull Class<PlayerBlockBreakEvent> eventType() {
        return PlayerBlockBreakEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerBlockBreakEvent event) {
        final boolean blockBreakEnabled = configManager.getConfig().getBoolean("protection.disable-build-protection");
        final boolean messageEnabled = configManager.getConfig().getBoolean("protection.block-break-message.enable");

        if (blockBreakEnabled) {
            event.setCancelled(true);

            if (messageEnabled) {
                final String message = configManager.getConfig().getString("protection.block-break-message.message");
                if (message != null && !message.isEmpty()) {
                    event.getPlayer().sendMessage(of(message).build());
                    return Result.INVALID;
                }
                LOGGER.warn("Block break message is not configured properly.");
            }

        }

        return Result.SUCCESS;
    }

}