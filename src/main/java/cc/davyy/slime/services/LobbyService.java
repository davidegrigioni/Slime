package cc.davyy.slime.services;

import cc.davyy.slime.model.Lobby;
import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface LobbyService {

    /**
     * Creates a new shared lobby instance with a unique name like "lobby1", "lobby2", etc.
     *
     * @return The newly created Lobby.
     */
    @NotNull Lobby createNewLobby();

    /**
     * Gets the main instance container that all shared lobbies are based on.
     *
     * @return The main lobby InstanceContainer.
     */
    @NotNull InstanceContainer getMainLobbyContainer();

    /**
     * Checks if the given instance is the main instance.
     *
     * @param instance The instance to check.
     * @return true if the instance is the main one, false otherwise.
     */
    boolean isMainInstance(@NotNull Instance instance);

    /**
     * Teleports the player to the specified lobby.
     *
     * @param player The player to teleport.
     * @param lobbyID The id of the lobby to teleport to.
     */
    void teleportPlayerToLobby(@NotNull SlimePlayer player, int lobbyID);

    /**
     * Lists all available lobbies.
     *
     * @return a collection of lobby ids.
     */
    @NotNull Collection<Integer> getAllLobbiesID();

    /**
     * Gets the lobby where the player currently is.
     *
     * @param player The player whose lobby needs to be found.
     * @return The Lobby where the player is, or null if not in a lobby.
     */
    Lobby getLobbyByPlayer(@NotNull SlimePlayer player);

    Collection<Lobby> getAllLobbies();

}