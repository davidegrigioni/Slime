package cc.davyy.slime.managers;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.ResourceBundle;

public class ChatTranslatorManager {

    private final TranslationRegistry registry;

    public ChatTranslatorManager() {
        // Create and register a TranslationRegistry
        this.registry = TranslationRegistry.create(Key.key("slime", "translations"));

        // Load and register English translations
        ResourceBundle enBundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);
        this.registry.registerAll(Locale.ENGLISH, enBundle, false);

        // Load and register French translations
        ResourceBundle frBundle = ResourceBundle.getBundle("messages", Locale.FRENCH);
        this.registry.registerAll(Locale.FRENCH, frBundle, false);

        // Register the registry with GlobalTranslator
        GlobalTranslator.translator().addSource(registry);
    }

    public void sendTranslatedMessage(@NotNull Player player, @NotNull String messageKey, String args) {
        Locale locale = player.getLocale();

        // Create a translatable component
        Component translatedComponent = Component.translatable(messageKey, args);

        // Translate the component using GlobalTranslator
        Component translatedMessage = GlobalTranslator.render(translatedComponent, locale);

        // Send the message with hover support
        sendChatMessageWithHover(player, translatedMessage, messageKey, args);
    }

    private void sendChatMessageWithHover(Player player, Component translatedMessage, String messageKey, String args) {
        // Serialize the original message key and args to a string for hover (optional)
        String originalMessage = PlainTextComponentSerializer.plainText().serialize(
                Component.translatable(messageKey, args)
        );

        Component messageComponent = translatedMessage.hoverEvent(
                HoverEvent.showText(Component.text(originalMessage))
        );

        player.sendMessage(messageComponent);
    }

}