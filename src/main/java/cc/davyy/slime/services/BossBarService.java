package cc.davyy.slime.services;

import cc.davyy.slime.model.SlimePlayer;
import org.jetbrains.annotations.NotNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.bossbar.BossBar;

import java.util.Map;
import java.util.UUID;

/**
 * Service interface for managing and displaying boss bars to players.
 */
public interface BossBarService {

    /**
     * Creates and displays a boss bar for the specified player.
     * <p>
     * This method creates a new boss bar with the given title, progress, color, and overlay, and displays it to the player.
     * If a boss bar already exists for the player, it may be replaced or updated depending on the implementation.
     * </p>
     *
     * @param player the player to show the boss bar to
     * @param title the title of the boss bar, displayed to the player
     * @param progress the progress of the boss bar, ranging from 0.0 (complete) to 1.0 (empty)
     * @param color the color of the boss bar, affecting its appearance
     * @param overlay the overlay type of the boss bar, defining its visual style
     */
    void createBossBar(@NotNull SlimePlayer player, @NotNull Component title,
                       float progress, @NotNull BossBar.Color color,
                       @NotNull BossBar.Overlay overlay);

    /**
     * Updates the boss bar of the specified player.
     * <p>
     * This method updates the title and progress of the existing boss bar for the player.
     * The boss bar must already be created for the player using {@link #createBossBar(SlimePlayer, Component, float, BossBar.Color, BossBar.Overlay)}.
     * </p>
     *
     * @param player the player whose boss bar will be updated
     * @param title the new title of the boss bar, displayed to the player
     * @param progress the new progress of the boss bar, ranging from 0.0 (complete) to 1.0 (empty)
     */
    void updateBossBar(@NotNull SlimePlayer player, @NotNull Component title, float progress);

    /**
     * Removes the boss bar from the specified player.
     * <p>
     * This method removes the boss bar from the player, making it no longer visible.
     * If no boss bar exists for the player, this method has no effect.
     * </p>
     *
     * @param player the player to remove the boss bar from
     */
    void removeBossBar(@NotNull SlimePlayer player);

    /**
     * Retrieves a map of all current boss bars for players.
     * <p>
     * This method returns a map where the keys are player UUIDs and the values are the corresponding boss bars currently displayed to those players.
     * </p>
     *
     * @return a map of player UUIDs to their corresponding boss bars
     */
    @NotNull
    Map<UUID, BossBar> getPlayerBossBars();

}
