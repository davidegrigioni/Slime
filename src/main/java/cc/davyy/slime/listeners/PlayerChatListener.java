package cc.davyy.slime.listeners;

import cc.davyy.slime.managers.ChatTranslatorManager;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerChatEvent;

public class PlayerChatListener {

    private final GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
    private final ChatTranslatorManager chatTranslatorManager;

    @Inject
    public PlayerChatListener(ChatTranslatorManager chatTranslatorManager) {
        this.chatTranslatorManager = chatTranslatorManager;
    }

    public void init() {
        globalEventHandler.addListener(PlayerChatEvent.class, event -> {
            final Player player = event.getPlayer();
            final String message = event.getMessage();

            event.setChatFormat(playerChatEvent -> {
                // Define the chat format, including the player's name and message
                String chatFormat = String.format("[%s] %s", player.getUsername(), message);

                // Detect and translate the message
                String translatedMessage = chatTranslatorManager.translateText(chatFormat, player.getLocale().getLanguage());

                // Return the formatted and translated message as a Component
                return Component.text(translatedMessage)
                        .hoverEvent(HoverEvent.showText(Component.text(message))); // Show original message on hover
            });
        });
    }

}