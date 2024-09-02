package cc.davyy.slime.managers;

import cc.davyy.slime.model.Lobby;
import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.*;

import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class LobbyManager {

    private static final AtomicInteger lobbyCounter = new AtomicInteger(1);
    private final InstanceContainer mainLobbyContainer;

    /**
     * Initializes the LobbyManager and creates a primary InstanceContainer for the lobbies.
     *
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
        String lobbyName = "lobby" + lobbyCounter.getAndIncrement();

        SharedInstance sharedInstance = MinecraftServer.getInstanceManager().createSharedInstance(mainLobbyContainer);
        return new Lobby(lobbyName, sharedInstance);
    }

    /**
     * Gets the main instance container that all shared lobbies are based on.
     *
     * @return The main lobby InstanceContainer.
     */
    public InstanceContainer getMainLobbyContainer() { return mainLobbyContainer; }

}