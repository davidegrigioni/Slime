package cc.davyy.slime.services.entities;

import cc.davyy.slime.model.SlimePlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Service interface for managing sidebars in the game.
 */
public interface SidebarService {

    /**
     * Displays the sidebar for the specified player.
     * <p>
     * This method will create and show a sidebar to the given player, displaying any
     * information or content that is configured for the sidebar.
     * </p>
     *
     * @param player the player to whom the sidebar will be shown.
     */
    void showSidebar(@NotNull SlimePlayer player);

    /**
     * Toggles the visibility of the sidebar for the specified player.
     * <p>
     * This method will switch the state of the sidebar between visible and hidden for the
     * given player. If the sidebar is currently visible, it will be hidden; if it is hidden,
     * it will be shown.
     * </p>
     *
     * @param player the player whose sidebar visibility will be toggled.
     */
    void toggleSidebar(@NotNull SlimePlayer player);

    /**
     * Removes the sidebar from the specified player.
     * <p>
     * This method will remove the sidebar from the player's view, effectively hiding it and
     * clearing any displayed information.
     * </p>
     *
     * @param player the player from whom the sidebar will be removed.
     */
    void removeSidebar(@NotNull SlimePlayer player);

}