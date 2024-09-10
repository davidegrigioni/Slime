package cc.davyy.slime.managers;

import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Singleton;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class BossBarManager {

    private final Map<UUID, BossBar> playerBossBars = new ConcurrentHashMap<>();

    /**
     * Creates and displays a boss bar for the specified player.
     *
     * @param player the player to show the boss bar to
     * @param title the title of the boss bar
     * @param progress the progress (between 0.0 and 1.0)
     * @param color the color of the boss bar
     * @param overlay the overlay type of the boss bar
     */
    public void createBossBar(@NotNull SlimePlayer player, @NotNull Component title, float progress, @NotNull BossBar.Color color, @NotNull BossBar.Overlay overlay) {
        BossBar bossBar = BossBar.bossBar(title, progress, color, overlay);
        player.showBossBar(bossBar);
        playerBossBars.put(player.getUuid(), bossBar);
    }

    /**
     * Updates the boss bar of the specified player.
     *
     * @param player the player whose boss bar will be updated
     * @param title the new title of the boss bar
     * @param progress the new progress (between 0.0 and 1.0)
     */
    public void updateBossBar(@NotNull SlimePlayer player, @NotNull Component title, float progress) {
        BossBar bossBar = playerBossBars.get(player.getUuid());
        if (bossBar != null) {
            BossBar updatedBossBar = bossBar.name(title).progress(progress);
            player.showBossBar(updatedBossBar);
        }
    }

    /**
     * Removes the boss bar from the specified player.
     *
     * @param player the player to remove the boss bar from
     */
    public void removeBossBar(@NotNull SlimePlayer player) {
        BossBar bossBar = playerBossBars.remove(player.getUuid());
        if (bossBar != null) {
            player.hideBossBar(bossBar);
        }
    }

}