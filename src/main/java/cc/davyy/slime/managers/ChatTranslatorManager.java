package cc.davyy.slime.managers;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.minestom.server.entity.Player;

import java.util.concurrent.TimeUnit;

@Singleton
public class ChatTranslatorManager {

    private final Translate translate = TranslateOptions.getDefaultInstance().getService();
    private final Cache<String, String> translationCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    public String detectLanguage(String message) {
        Detection detection = translate.detect(message);
        return detection.getLanguage();
    }

    public String translateText(String message, String targetLanguage) {
        String cacheKey = message + "|" + targetLanguage;

        // Check if translation is cached
        String cachedTranslation = translationCache.getIfPresent(cacheKey);
        if (cachedTranslation != null) {
            return cachedTranslation;
        }

        // Perform translation and cache the result
        Translation translation = translate.translate(message, Translate.TranslateOption.targetLanguage(targetLanguage));
        String translatedText = translation.getTranslatedText();
        translationCache.put(cacheKey, translatedText);

        return translatedText;
    }

    public void sendTranslatedMessage(Player player, String originalMessage) {
        String detectedLanguage = detectLanguage(originalMessage);
        String playerLocale = player.getLocale().getLanguage();

        if (!detectedLanguage.equals(playerLocale)) {
            String translatedMessage = translateText(originalMessage, playerLocale);
            sendChatMessageWithHover(player, originalMessage, translatedMessage);
        } else {
            player.sendMessage(originalMessage);
        }
    }

    private void sendChatMessageWithHover(Player player, String originalMessage, String translatedMessage) {
        Component messageComponent = Component.text(originalMessage)
                .hoverEvent(HoverEvent.showText(Component.text(translatedMessage)));

        player.sendMessage(messageComponent);
    }

}