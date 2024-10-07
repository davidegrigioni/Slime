package cc.davyy.slime.listeners;

import cc.davyy.slime.config.ConfigManager;
import cc.davyy.slime.database.DatabaseManager;
import cc.davyy.slime.database.entities.PlayerProfile;
import cc.davyy.slime.factories.PlayerFactory;
import cc.davyy.slime.managers.entities.SidebarManager;
import cc.davyy.slime.managers.entities.nametag.NameTag;
import cc.davyy.slime.managers.entities.nametag.NameTagManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerSpawnEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.JoinUtils.applyJoinKit;
import static net.minestom.server.MinecraftServer.LOGGER;

@Singleton
public class PlayerJoinListener implements EventListener<PlayerSpawnEvent> {

    private final PlayerFactory playerFactory;

    private final DatabaseManager databaseManager;
    private final ConfigManager configManager;
    private final NameTagManager nameTagManager;
    private final SidebarManager sidebarManager;

    @Inject
    public PlayerJoinListener(PlayerFactory playerFactory, DatabaseManager databaseManager, ConfigManager configManager, NameTagManager nameTagManager, SidebarManager sidebarManager) {
        this.playerFactory = playerFactory;
        this.databaseManager = databaseManager;
        this.configManager = configManager;
        this.nameTagManager = nameTagManager;
        this.sidebarManager = sidebarManager;
    }

    @Override
    public @NotNull Class<PlayerSpawnEvent> eventType() {
        return PlayerSpawnEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerSpawnEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();

        createProfile(player);

        player.setGameMode(GameMode.ADVENTURE);

        sendHeaderFooter(player);

        setNameTags(player, player.getPrefix()
                .append(Component.text(" "))
                .append(player.getName()));

        sidebarManager.showSidebar(player);

        applyJoinKit(player, configManager);

        return Result.SUCCESS;
    }

    private void sendHeaderFooter(@NotNull SlimePlayer player) {
        final List<String> headerList = configManager.getUi().getStringList("header");
        final List<String> footerList = configManager.getUi().getStringList("footer");

        final String header = String.join("\n", headerList);
        final String footer = String.join("\n", footerList);

        player.sendPlayerListHeaderAndFooter(
                of(header).build(),
                of(footer).build()
        );
    }

    private void setNameTags(@NotNull SlimePlayer player, @NotNull Component text) {
        final NameTag nameTag = nameTagManager.createNameTag(player);

        nameTag.setText(text);
        nameTag.addViewer(player);
        nameTag.mount();
    }

    private void createProfile(@NotNull SlimePlayer player) {
        databaseManager.hasPlayerProfile(player.getUuid()).thenAcceptAsync(hasProfile -> {
            if (!hasProfile) {
                PlayerProfile playerProfile = playerFactory.createNewProfile();
                databaseManager.saveOrUpdatePlayerProfile(playerProfile).thenRun(() -> LOGGER.info("Created new PlayerProfile for player: {}", player.getUsername()));
            } else {
                LOGGER.info("PlayerProfile already exists for player: {}", player.getUsername());
            }
        });
    }

}