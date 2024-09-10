package cc.davyy.slime.listeners;

import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerChatEvent;

public class PlayerChatListener {

    public void init(GlobalEventHandler handler) {
        handler.addListener(PlayerChatEvent.class, event -> {
            final SlimePlayer player = (SlimePlayer) event.getPlayer();

            event.setChatFormat(chatEvent -> {
                final String message = chatEvent.getMessage();
                return player.getChatFormat(message);
            });
        });
    }

}