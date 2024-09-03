package cc.davyy.slime.utils;

import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ChatTranslatorUtils {

    private static final Translate translate = TranslateOptions.getDefaultInstance().getService();
    private static final Map<String, String> translationCache = new ConcurrentHashMap<>();

    private ChatTranslatorUtils() {}

    public static String detectLanguage(@NotNull String message) {
        Detection detection = translate.detect(message);
        return detection.getLanguage();
    }

    public static String translateText(String message, String targetLanguage) {
        // Create a cache key using the message and target language
        String cacheKey = message + "|" + targetLanguage;

        // Check if the translation is already in the cache
        if (translationCache.containsKey(cacheKey)) {
            return translationCache.get(cacheKey);
        }

        // If not, perform the translation
        Translation translation = translate.translate(message, Translate.TranslateOption.targetLanguage(targetLanguage));
        String translatedText = translation.getTranslatedText();

        // Store the translation in the cache
        translationCache.put(cacheKey, translatedText);

        return translatedText;
    }

    public static void sendTranslatedMessage(@NotNull Player player, @NotNull String originalMessage) {
        final String detectedLanguage = detectLanguage(originalMessage);
        final Locale locale = player.getLocale();

        Check.notNull(locale, "Locale cannot be null");
        final String playerLocale = locale.getDisplayLanguage();

        if (!detectedLanguage.equals(playerLocale)) {
            final String translatedMessage = translateText(originalMessage, playerLocale);
            sendChatMessageWithHover(player, originalMessage, translatedMessage);
            return;
        }

        player.sendMessage(originalMessage);
    }

    private static void sendChatMessageWithHover(Player player, String originalMessage, String translatedMessage) {
        final Component messageComponent = Component.text(originalMessage)
                .hoverEvent(HoverEvent.showText(Component.text(translatedMessage)));

        player.sendMessage(messageComponent);
    }

}