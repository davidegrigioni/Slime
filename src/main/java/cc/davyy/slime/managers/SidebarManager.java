package cc.davyy.slime.managers;

import cc.davyy.slime.constants.TagConstants;
import cc.davyy.slime.managers.gameplay.LobbyManager;
import cc.davyy.slime.managers.general.ConfigManager;
import cc.davyy.slime.services.gameplay.SidebarService;
import cc.davyy.slime.model.Lobby;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.minestom.server.MinecraftServer;
import net.minestom.server.scoreboard.Sidebar;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.GeneralUtils.getOnlineSlimePlayers;

@Singleton
public class SidebarManager implements SidebarService {

    private final String sidebarTitle;
    private final LobbyManager lobbyManager;

    @Inject
    public SidebarManager(@Named("scoreboardTitle") String sidebarTitle, ConfigManager configManager, LobbyManager lobbyManager) {
        this.sidebarTitle = sidebarTitle;
        this.lobbyManager = lobbyManager;

        final List<String> lines = configManager.getUi().getStringList("scoreboard.lines");
        MinecraftServer.getSchedulerManager().buildTask(() -> updateSidebarLines(lines))
                .repeat(1, ChronoUnit.SECONDS).schedule();
    }

    @Override
    public void showSidebar(@NotNull SlimePlayer player) {
        if (!player.hasTag(TagConstants.SIDEBAR_TAG)) {
            final Sidebar playerSidebar = new Sidebar(of(sidebarTitle)
                    .build());
            playerSidebar.addViewer(player);
            player.setTag(TagConstants.SIDEBAR_TAG, playerSidebar);
        }
    }

    @Override
    public void toggleSidebar(@NotNull SlimePlayer player) {
        final Sidebar playerSidebar = player.getTag(TagConstants.SIDEBAR_TAG);

        if (playerSidebar != null) {
            if (!playerSidebar.getViewers().contains(player)) {
                playerSidebar.addViewer(player);
                player.setTag(TagConstants.SIDEBAR_TAG, playerSidebar);
                return;
            }

            playerSidebar.removeViewer(player);
            player.removeTag(TagConstants.SIDEBAR_TAG);
        }
    }

    @Override
    public void removeSidebar(@NotNull SlimePlayer player) {
        final Sidebar playerSidebar = player.getTag(TagConstants.SIDEBAR_TAG);

        if (playerSidebar != null) {
            playerSidebar.removeViewer(player);
            player.removeTag(TagConstants.SIDEBAR_TAG);
        }
    }

    private void updateSidebarLines(@NotNull List<String> lines) {
        for (SlimePlayer player : getOnlineSlimePlayers()) {
            updatePlayerSidebar(player, lines);
        }
    }

    private void updatePlayerSidebar(@NotNull SlimePlayer player, @NotNull List<String> lines) {
        final Sidebar playerSidebar = player.getTag(TagConstants.SIDEBAR_TAG);

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
        final Sidebar playerSidebar = player.getTag(TagConstants.SIDEBAR_TAG);
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