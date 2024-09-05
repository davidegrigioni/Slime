package cc.davyy.slime.listeners;

import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerChatEvent;

import static net.kyori.adventure.text.Component.text;

public class PlayerChatListener {

    public void init(GlobalEventHandler handler) {
        handler.addListener(PlayerChatEvent.class, event -> {
            final SlimePlayer player = (SlimePlayer) event.getPlayer();
            event.setChatFormat(e -> text().append(
                    player.getPrefix()
                            .append(text(" ")),
                    player.getName(),
                    text(": "),
                    text(e.getMessage()))
                    .build());
        });
    }

}