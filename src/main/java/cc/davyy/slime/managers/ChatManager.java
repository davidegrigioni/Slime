package cc.davyy.slime.managers;

import cc.davyy.slime.managers.general.ConfigManager;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.ColorUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.platform.PlayerAdapter;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

@Singleton
public class ChatManager {

    private final PlayerAdapter<Player> playerAdapter;
    private final ConfigManager configManager;

    @Inject
    public ChatManager(PlayerAdapter<Player> playerAdapter, ConfigManager configManager) {
        this.playerAdapter = playerAdapter;
        this.configManager = configManager;
    }

    public @NotNull Component setChatFormat(@NotNull SlimePlayer player, @NotNull String message) {
        final CachedMetaData metaData = playerAdapter.getMetaData(player);
        final String group = metaData.getPrimaryGroup();
        final String groupFormat = configManager.getConfig().getString("group-formats." + group) != null ? "group-formats." + group : "chat-format";
        final String format = configManager.getConfig().getString(groupFormat);

        return ColorUtils.of(format)
                .addPlainStringPlaceholder("lobbyid", String.valueOf(player.getLobbyID()))
                .addFormattedStringPlaceholder("prefix", metaData.getPrefix() != null ? metaData.getPrefix() : "")
                .addFormattedStringPlaceholder("message", message)
                .addComponentPlaceholder("name", player.getName())
                .addFormattedStringPlaceholder("username-color", metaData.getMetaValue("username-color") != null ? metaData.getMetaValue("username-color") : "")
                .addFormattedStringPlaceholder("message-color", metaData.getMetaValue("message-color") != null ? metaData.getMetaValue("message-color") : "")
                .build();
    }

    public @NotNull Component setDefaultChatFormat(@NotNull SlimePlayer player, @NotNull String message) {
        final String format = configManager.getConfig().getString("chat-format");

        return ColorUtils.of(format)
                .addComponentPlaceholder("prefix", player.getPrefix())
                .addPlainStringPlaceholder("lobbyid", String.valueOf(player.getLobbyID()))
                .addPlainStringPlaceholder("message", message)
                .addComponentPlaceholder("name", player.getName())
                .build();
    }

}