package cc.davyy.slime.services;

import cc.davyy.slime.model.Lobby;
import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Service interface for managing lobbies in the game.
 */
public interface LobbyService {

    /**
     * Creates a new shared lobby instance with a unique name like "lobby1", "lobby2", etc.
     * <p>
     * This method initializes a new lobby with a unique identifier and returns the newly created
     * {@link Lobby} object.
     * </p>
     *
     * @return The newly created {@link Lobby}.
     */
    @NotNull Lobby createNewLobby();

    /**
     * Retrieves the main instance container that all shared lobbies are based on.
     * <p>
     * The main lobby instance container serves as the core container from which all shared lobbies
     * derive their properties and functionality.
     * </p>
     *
     * @return The main {@link InstanceContainer} for lobbies.
     */
    @NotNull InstanceContainer getMainLobbyContainer();

    /**
     * Checks if the given instance is the main instance.
     * <p>
     * This method verifies whether the provided {@link Instance} is the main instance container.
     * </p>
     *
     * @param instance The {@link Instance} to check.
     * @return {@code true} if the instance is the main one, {@code false} otherwise.
     */
    boolean isMainInstance(@NotNull Instance instance);

    /**
     * Teleports the specified player to the given lobby.
     * <p>
     * This method will move the player identified by the given {@link SlimePlayer} to the lobby with
     * the specified ID.
     * </p>
     *
     * @param player The {@link SlimePlayer} to teleport.
     * @param lobbyID The ID of the lobby to teleport to.
     */
    void teleportPlayerToLobby(@NotNull SlimePlayer player, int lobbyID);

    /**
     * Lists all available lobbies.
     * <p>
     * This method returns a collection of IDs representing all available lobbies in the game.
     * </p>
     *
     * @return a {@link Collection} of lobby IDs.
     */
    @NotNull Collection<Integer> getAllLobbiesID();

    /**
     * Retrieves the lobby where the specified player is currently located.
     * <p>
     * This method will find and return the {@link Lobby} associated with the provided player. If
     * the player is not in any lobby, {@code null} will be returned.
     * </p>
     *
     * @param player The {@link SlimePlayer} whose lobby needs to be found.
     * @return The {@link Lobby} where the player is located, or {@code null} if not in a lobby.
     */
    Lobby getLobbyByPlayer(@NotNull SlimePlayer player);

    /**
     * Retrieves all lobbies currently available.
     * <p>
     * This method returns a collection of all {@link Lobby} objects currently managed by the service.
     * </p>
     *
     * @return a {@link Collection} of {@link Lobby} objects.
     */
    @NotNull Collection<Lobby> getAllLobbies();

}