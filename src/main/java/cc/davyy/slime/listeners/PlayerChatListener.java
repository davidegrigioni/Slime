package cc.davyy.slime.listeners;

import cc.davyy.slime.managers.ChatManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PlayerChatListener implements EventListener<PlayerChatEvent> {

    private final ChatManager chatManager;

    @Inject
    public PlayerChatListener(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    @Override
    public @NotNull Class<PlayerChatEvent> eventType() {
        return PlayerChatEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerChatEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();
        final String message = event.getMessage();
        final Instance instance = player.getInstance();

        final Component formattedMessage;
        if (player.hasPermission("slime.colorchat")) {
            formattedMessage = chatManager.setChatFormat(player, message);
        } else {
            formattedMessage = chatManager.setDefaultChatFormat(player, message);
        }

        event.setCancelled(true);

        instance.getPlayers().forEach(players -> {
            if (players instanceof SlimePlayer slimePlayer) {
                slimePlayer.sendMessage(formattedMessage);
            }
        });

        return Result.SUCCESS;
    }

}