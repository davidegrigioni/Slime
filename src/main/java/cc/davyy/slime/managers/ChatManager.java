package cc.davyy.slime.managers;

import cc.davyy.slime.managers.general.ConfigManager;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.ColorUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

@Singleton
public class ChatManager {

    private final String defaultChatFormat;
    private final ConfigManager configManager;

    @Inject
    public ChatManager(@Named("defaultChatFormat") String defaultChatFormat, ConfigManager configManager) {
        this.defaultChatFormat = defaultChatFormat;
        this.configManager = configManager;
    }

    public @NotNull Component setChatFormat(@NotNull SlimePlayer player, @NotNull String message) {
        final String group = player.getPrimaryGroup();
        final String groupFormat = configManager.getConfig().getString("group-formats." + group) != null ? "group-formats." + group : "chat-format";
        final String format = configManager.getConfig().getString(groupFormat);

        return ColorUtils.of(format)
                .addPlainStringPlaceholder("lobbyid", String.valueOf(player.getLobbyID()))
                .addFormattedStringPlaceholder("prefix", player.getPlainPrefix() != null ? player.getPlainPrefix() : "")
                .addFormattedStringPlaceholder("message", message)
                .addComponentPlaceholder("name", player.getName())
                .addFormattedStringPlaceholder("username-color", player.getMetaValue("username-color") != null ? player.getMetaValue("username-color") : "")
                .addFormattedStringPlaceholder("message-color", player.getMetaValue("message-color") != null ? player.getMetaValue("message-color") : "")
                .build();
    }

    public @NotNull Component setDefaultChatFormat(@NotNull SlimePlayer player, @NotNull String message) {
        return ColorUtils.of(defaultChatFormat)
                .addComponentPlaceholder("prefix", player.getPrefix())
                .addPlainStringPlaceholder("lobbyid", String.valueOf(player.getLobbyID()))
                .addPlainStringPlaceholder("message", message)
                .addComponentPlaceholder("name", player.getName())
                .build();
    }

}