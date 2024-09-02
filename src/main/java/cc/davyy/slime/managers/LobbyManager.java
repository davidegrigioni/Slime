package cc.davyy.slime.managers;

import cc.davyy.slime.model.Lobby;
import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.*;
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
     * Teleports the player to the specified lobby.
     *
     * @param player The player to teleport.
     * @param lobbyID The id of the lobby to teleport to.
     */
    public void teleportPlayerToLobby(@NotNull Player player, int lobbyID) {
        final Lobby lobby = lobbies.get(lobbyID);

        Check.notNull(lobby, "Lobby " + lobbyID + " not found!");

        player.setInstance(lobby.sharedInstance()).thenRun(() -> player.sendMessage("Teleported to " + lobbyID));
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