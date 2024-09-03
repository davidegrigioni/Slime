package cc.davyy.slime.listeners;

import cc.davyy.slime.utils.ChatTranslatorUtils;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

public class PlayerChatListener implements EventListener<PlayerChatEvent> {

    @Override
    public @NotNull Class<PlayerChatEvent> eventType() { return PlayerChatEvent.class; }

    @Override
    public @NotNull Result run(@NotNull PlayerChatEvent event) {
        final Player player = event.getPlayer();
        final Instance instance = player.getInstance();
        final String message = event.getMessage();

        event.setCancelled(true);

        instance.getPlayers().forEach(instancePlayer -> ChatTranslatorUtils.sendTranslatedMessage(player, message));

        return Result.SUCCESS;
    }

}