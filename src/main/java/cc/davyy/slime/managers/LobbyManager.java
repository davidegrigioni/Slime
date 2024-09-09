package cc.davyy.slime.managers;

import cc.davyy.slime.model.Lobby;
import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.*;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static net.minestom.server.MinecraftServer.LOGGER;

@Singleton
public class LobbyManager {

    private static final AtomicInteger lobbyIDCounter = new AtomicInteger(1);
    private static final AtomicInteger lobbyCounter = new AtomicInteger(1);

    private final InstanceContainer mainLobbyContainer;
    private final Map<Integer, Lobby> lobbies = new ConcurrentHashMap<>();

    /**
     * Initializes the LobbyManager and creates a primary InstanceContainer for the lobbies.
     */
    public LobbyManager() {
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        this.mainLobbyContainer = instanceManager.createInstanceContainer();
        this.mainLobbyContainer.setChunkLoader(IChunkLoader.noop());
        this.mainLobbyContainer.setChunkSupplier(LightingChunk::new);
        this.mainLobbyContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 20, Block.GRASS_BLOCK));
    }

    /**
     * Creates a new shared lobby instance with a unique name like "lobby1", "lobby2", etc.
     *
     * @return The newly created Lobby.
     */
    public Lobby createNewLobby() {
        final String lobbyName = "Lobby " + lobbyCounter.getAndIncrement();
        final int lobbyID = lobbyIDCounter.getAndIncrement();

        final SharedInstance sharedInstance = MinecraftServer.getInstanceManager().createSharedInstance(mainLobbyContainer);
        sharedInstance.setChunkSupplier(LightingChunk::new);

        Lobby lobby = new Lobby(lobbyID, lobbyName, sharedInstance);
        lobbies.put(lobbyID, lobby);
        return lobby;
    }

    /**
     * Gets the main instance container that all shared lobbies are based on.
     *
     * @return The main lobby InstanceContainer.
     */
    public InstanceContainer getMainLobbyContainer() { return mainLobbyContainer; }

    /**
     * Checks if the given instance is the main instance.
     *
     * @param instance The instance to check.
     * @return true if the instance is the main one, false otherwise.
     */
    public boolean isMainInstance(@NotNull Instance instance) {
        return instance.equals(mainLobbyContainer);
    }

    /**
     * Teleports the player to the specified lobby.
     *
     * @param player The player to teleport.
     * @param lobbyID The id of the lobby to teleport to.
     */
    public void teleportPlayerToLobby(@NotNull Player player, int lobbyID) {
        if (lobbyID == 0) {
            if (player.getInstance() == mainLobbyContainer) {
                player.sendMessage("You are already in the main lobby!");
                return;
            }
            player.setInstance(mainLobbyContainer).thenRun(() -> player.sendMessage("Teleported to the main lobby."))
                    .exceptionally(ex -> {
                        LOGGER.error("Failed to teleport player to the main lobby", ex);
                        player.sendMessage("Failed to teleport to the main lobby. Please try again.");
                        return null;
                    });
            return;
        }

        final Lobby lobby = lobbies.get(lobbyID);

        if (lobby == null) {
            LOGGER.error("Lobby {} not found!", lobbyID);
            player.sendMessage("Lobby not found!");
            return;
        }

        if (player.getInstance() == lobby.sharedInstance()) {
            player.sendMessage("You are already in this lobby!");
            return;
        }

        player.setInstance(lobby.sharedInstance())
                .thenRun(() -> player.sendMessage("Teleported to " + lobbyID))
                .exceptionally(ex -> {
                    LOGGER.error("Failed to teleport player to lobby {}", lobbyID, ex);
                    player.sendMessage("Failed to teleport. Please try again.");
                    return null;
                });
    }

    /**
     * Lists all available lobbies.
     *
     * @return a collection of lobby ids.
     */
    public Collection<Integer> getAllLobbiesID() { return new ArrayList<>(lobbies.keySet()); }

    /**
     * Gets the lobby where the player currently is.
     *
     * @param player The player whose lobby needs to be found.
     * @return The Lobby where the player is, or null if not in a lobby.
     */
    public Lobby getLobbyByPlayer(@NotNull Player player) {
        Instance playerInstance = player.getInstance();
        if (playerInstance == null) {
            // Handle the null case, maybe return a default lobby or log an error
            LOGGER.error("Player's instance is null for player {}", player.getUsername());
            return null;
        }

        if (isMainInstance(playerInstance)) {
            return null; // Player is in the main lobby, no specific lobby assigned
        }

        return lobbies.values().stream()
                .filter(lobby -> lobby.sharedInstance().equals(playerInstance))
                .findFirst()
                .orElse(null); // Return null if no lobby matches
    }

}