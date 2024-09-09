package cc.davyy.slime.managers;

import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static cc.davyy.slime.utils.FileUtils.getConfig;
import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.GeneralUtils.getOnlinePlayers;

@Singleton
public class SidebarManager {

    private final Sidebar sidebar;
    private final Map<UUID, Sidebar> sidebarMap = new ConcurrentHashMap<>();

    public SidebarManager() {
        final String sidebarTitle = getConfig().getString("scoreboard.title");
        this.sidebar = new Sidebar(of(sidebarTitle).build());

        final List<String> lines = getConfig().getStringList("scoreboard.lines");
        MinecraftServer.getSchedulerManager().buildTask(() -> updateSidebarLines(lines))
                .repeat(1, TimeUnit.SECONDS.toChronoUnit()).schedule();
    }

    private void updateSidebarLines(@NotNull List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            String text = lines.get(i);
            int score = lines.size() - i;
            updateOrAddLine(text, score);
        }
    }

    private void updateOrAddLine(@NotNull String text, int score) {
        final String lineId = "line-" + score;
        final var onlinePlayersSize = getOnlinePlayers().size();

        // Check if the line already exists
        if (sidebar.getLine(lineId) != null) {
            sidebar.updateLineContent(lineId, of(text)
                    .parseMMP("playercount", String.valueOf(onlinePlayersSize))
                    .build());
            return;
        }

        // Create the line with dynamic player count
        sidebar.createLine(new Sidebar.ScoreboardLine(lineId,
                of(text)
                        .parseMMP("playercount", String.valueOf(onlinePlayersSize))
                        .build(), score, Sidebar.NumberFormat.blank()));
    }

    public void showSidebar(@NotNull Player player) {
        if (!sidebarMap.containsKey(player.getUuid())) {
            sidebar.addViewer(player);
            sidebarMap.put(player.getUuid(), sidebar);
        }
    }

    public void removeSidebar(@NotNull Player player) {
        final Sidebar sidebar = sidebarMap.get(player.getUuid());
        if (sidebar != null) {
            sidebarMap.remove(player.getUuid());
            sidebar.removeViewer(player);
        }
    }

}