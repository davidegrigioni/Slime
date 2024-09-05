package cc.davyy.slime.managers;

import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static cc.davyy.slime.utils.FileUtils.getConfig;
import static cc.davyy.slime.utils.ColorUtils.of;

@Singleton
public class SidebarManager {

    private final Sidebar sidebar;
    private final Map<UUID, Sidebar> sidebarMap = new HashMap<>();

    public SidebarManager() {
        String sidebarTitle = getConfig().getString("scoreboard.title");
        this.sidebar = new Sidebar(of(sidebarTitle)
                .build());

        List<String> lines = getConfig().getStringList("scoreboard.lines");
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            for (int i = 0; i < lines.size(); i++) {
                addLine(lines.get(i), lines.size() - i);
            }
        }).repeat(1, TimeUnit.SECONDS.toChronoUnit()).schedule();

    }

    public void showSidebar(@NotNull Player player) {
        sidebar.addViewer(player);
        sidebarMap.put(player.getUuid(), sidebar);
    }

    public void removeSidebar(@NotNull Player player) {
        if (sidebarMap.containsKey(player.getUuid())) {
            sidebarMap.get(player.getUuid()).removeViewer(player);
            sidebarMap.remove(player.getUuid());
        }
    }

    public void addLine(@NotNull String text, int score) {
        String lineId = "line-" + score;

        // Remove existing line with the same ID before adding the new one
        sidebar.removeLine(lineId);

        // Create the line with dynamic player count
        sidebar.createLine(new Sidebar.ScoreboardLine(lineId,
                of(text)
                        .parseMMP("playercount", String.valueOf(MinecraftServer.getConnectionManager().getOnlinePlayers().size()))
                        .build(), score, Sidebar.NumberFormat.blank()));
    }

    /**
     * Updates a specific line in the sidebar.
     *
     * @param lineId The ID of the line to update.
     * @param text   The new text.
     */
    public void updateLine(@NotNull String lineId, @NotNull String text) {
        sidebar.updateLineContent(lineId, of(text)
                .build());
    }

}