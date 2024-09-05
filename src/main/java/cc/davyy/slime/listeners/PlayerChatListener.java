package cc.davyy.slime.listeners;

import cc.davyy.slime.managers.ChatTranslatorManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerChatEvent;

public class PlayerChatListener {

    private final ChatTranslatorManager chatTranslatorManager;

    @Inject
    public PlayerChatListener(ChatTranslatorManager chatTranslatorManager) {
        this.chatTranslatorManager = chatTranslatorManager;
    }

    public void init(GlobalEventHandler handler) {
        handler.addListener(PlayerChatEvent.class, event -> {
            final SlimePlayer player = (SlimePlayer) event.getPlayer();
            event.setChatFormat(e -> Component.text().append(
                    player.getPrefix(),
                    player.getName(),
                    Component.text(": "),
                    Component.text(e.getMessage()))
                    .build());
        });
    }

}