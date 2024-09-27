package cc.davyy.slime.managers;

import cc.davyy.slime.managers.general.ConfigManager;
import cc.davyy.slime.services.gameplay.SidebarService;
import cc.davyy.slime.model.Lobby;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.scoreboard.Sidebar;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.GeneralUtils.getOnlineSlimePlayers;

@Singleton
public class SidebarManager implements SidebarService {

    private static final String SIDEBAR_TITLE = "scoreboard.title";

    private final ConfigManager configManager;
    private final LobbyManager lobbyManager;
    private final Map<UUID, Sidebar> sidebarMap = new ConcurrentHashMap<>();

    @Inject
    public SidebarManager(ConfigManager configManager, LobbyManager lobbyManager) {
        this.configManager = configManager;
        this.lobbyManager = lobbyManager;

        final List<String> lines = configManager.getConfig().getStringList("scoreboard.lines");
        MinecraftServer.getSchedulerManager().buildTask(() -> updateSidebarLines(lines))
                .repeat(1, ChronoUnit.SECONDS).schedule();
    }

    @Override
    public void showSidebar(@NotNull SlimePlayer player) {
        if (!sidebarMap.containsKey(player.getUuid())) {
            final String sidebarTitle = configManager.getConfig().getString(SIDEBAR_TITLE);
            final Sidebar playerSidebar = new Sidebar(of(sidebarTitle)
                    .build());
            playerSidebar.addViewer(player);
            sidebarMap.put(player.getUuid(), playerSidebar);
        }
    }

    @Override
    public void toggleSidebar(@NotNull SlimePlayer player) {
        final Sidebar playerSidebar = sidebarMap.get(player.getUuid());

        if (playerSidebar != null) {
            if (!playerSidebar.getViewers().contains(player)) {
                playerSidebar.addViewer(player);
                return;
            }

            playerSidebar.removeViewer(player);
        }
    }

    @Override
    public void removeSidebar(@NotNull SlimePlayer player) {
        final Sidebar playerSidebar = sidebarMap.remove(player.getUuid());

        if (playerSidebar != null) {
            playerSidebar.removeViewer(player);
        }
    }

    private void updateSidebarLines(@NotNull List<String> lines) {
        for (SlimePlayer player : getOnlineSlimePlayers()) {
            updatePlayerSidebar(player, lines);
        }
    }

    private void updatePlayerSidebar(@NotNull SlimePlayer player, @NotNull List<String> lines) {
        final Sidebar playerSidebar = sidebarMap.get(player.getUuid());

        if (playerSidebar != null) {
            for (int i = 0; i < lines.size(); i++) {
                String text = lines.get(i);
                int score = lines.size() - i;
                updateOrAddLine(text, score, player);
            }
        }
    }

    private void updateOrAddLine(@NotNull String text, int score, @NotNull SlimePlayer player) {
        final String lineId = "line-" + score;
        final Sidebar playerSidebar = sidebarMap.get(player.getUuid());
        final int onlinePlayersSize = getOnlineSlimePlayers().size();

        if (playerSidebar != null) {
            final Lobby playerLobby = lobbyManager.getLobbyByPlayer(player);
            final String lobbyName = playerLobby != null ? playerLobby.name() : "Main Lobby";

            if (playerSidebar.getLine(lineId) != null) {
                playerSidebar.updateLineContent(lineId, of(text)
                        .addPlainStringPlaceholder("lobby", lobbyName)
                        .addComponentPlaceholder("rank", player.getPrefix())
                        .addPlainStringPlaceholder("playercount", String.valueOf(onlinePlayersSize))
                        .build());
                return;
            }

            final Sidebar.ScoreboardLine scoreboardLine = new Sidebar.ScoreboardLine(lineId,
                    of(text)
                            .addFormattedStringPlaceholder("lobby", lobbyName)
                            .addComponentPlaceholder("rank", player.getPrefix())
                            .addPlainStringPlaceholder("playercount", String.valueOf(onlinePlayersSize))
                            .build(), score, Sidebar.NumberFormat.blank());

            playerSidebar.createLine(scoreboardLine);
        }
    }

}