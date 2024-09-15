package cc.davyy.slime.services;

import cc.davyy.slime.model.SlimePlayer;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public interface BossBarService {

    /**
     * Creates and displays a boss bar for the specified player.
     *
     * @param player the player to show the boss bar to
     * @param title the title of the boss bar
     * @param progress the progress (between 0.0 and 1.0)
     * @param color the color of the boss bar
     * @param overlay the overlay type of the boss bar
     */
    void createBossBar(@NotNull SlimePlayer player, @NotNull Component title,
                       float progress, @NotNull BossBar.Color color,
                       @NotNull BossBar.Overlay overlay);

    /**
     * Updates the boss bar of the specified player.
     *
     * @param player the player whose boss bar will be updated
     * @param title the new title of the boss bar
     * @param progress the new progress (between 0.0 and 1.0)
     */
    void updateBossBar(@NotNull SlimePlayer player, @NotNull Component title, float progress);

    /**
     * Removes the boss bar from the specified player.
     *
     * @param player the player to remove the boss bar from
     */
    void removeBossBar(@NotNull SlimePlayer player);

    Map<UUID, BossBar> getPlayerBossBars();

}