package cc.davyy.slime.managers;

import cc.davyy.slime.model.Lobby;
import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.*;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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
        final String lobbyName = "lobby" + lobbyCounter.getAndIncrement();
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
    public boolean isMainInstance(@NotNull Instance instance) { return instance.equals(mainLobbyContainer); }

    /**
     * Teleports the player to the specified lobby.
     *
     * @param player The player to teleport.
     * @param lobbyID The id of the lobby to teleport to.
     */
    public void teleportPlayerToLobby(@NotNull Player player, int lobbyID) {
        Instance targetInstance;

        if (lobbyID == 0) {
            // Teleport to the main instance if lobbyID is 0
            targetInstance = mainLobbyContainer;
        } else {
            // Find the lobby with the given ID
            Lobby lobby = lobbies.get(lobbyID);
            Check.notNull(lobby, "Lobby " + lobbyID + " not found!");
            targetInstance = lobby.sharedInstance();
        }

        // Teleport the player to the target instance
        player.setInstance(targetInstance).thenRun(() -> player.sendMessage("Teleported to " + (lobbyID == 0 ? "main instance" : "lobby " + lobbyID)));
    }

    /**
     * Lists all available lobbies.
     *
     * @return a collection of lobby ids.
     */
    public Collection<Integer> getAllLobbiesID() {
        return new ArrayList<>(lobbies.keySet());
    }

}