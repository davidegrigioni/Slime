package cc.davyy.slime.managers;

import cc.davyy.slime.model.Lobby;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import cc.davyy.slime.misc.TagConstants;
import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.*;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static cc.davyy.slime.utils.FileUtils.getConfig;

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
        final InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        this.mainLobbyContainer = instanceManager.createInstanceContainer();
        this.mainLobbyContainer.setChunkSupplier(LightingChunk::new);
        this.mainLobbyContainer.setChunkLoader(IChunkLoader.noop());
        this.mainLobbyContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 20, Block.GRASS_BLOCK));
        this.mainLobbyContainer.setTag(TagConstants.DEATH_Y, getConfig().getInt("deathY"));
    }

    /**
     * Creates a new shared lobby instance with a unique name like "lobby1", "lobby2", etc.
     *
     * @return The newly created Lobby.
     */
    public Lobby createNewLobby() {
        final String lobbyName = "Lobby " + lobbyCounter.getAndIncrement();
        final int lobbyID = lobbyIDCounter.getAndIncrement();
        final int deathY = getConfig().getInt("deathY");

        final SharedInstance sharedInstance = MinecraftServer.getInstanceManager().createSharedInstance(mainLobbyContainer);
        sharedInstance.setChunkSupplier(LightingChunk::new);

        Lobby lobby = new Lobby(lobbyID, lobbyName, sharedInstance);
        lobbies.put(lobbyID, lobby);

        sharedInstance.setTag(TagConstants.LOBBY_NAME_TAG, lobbyName);
        sharedInstance.setTag(TagConstants.LOBBY_ID_TAG, lobbyID);
        sharedInstance.setTag(TagConstants.DEATH_Y, deathY);

        return lobby;
    }

    /**
     * Gets the main instance container that all shared lobbies are based on.
     *
     * @return The main lobby InstanceContainer.
     */
    public @NotNull InstanceContainer getMainLobbyContainer() { return mainLobbyContainer; }

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
    public void teleportPlayerToLobby(@NotNull SlimePlayer player, int lobbyID) {
        if (lobbyID == 0) {
            if (player.getInstance() == mainLobbyContainer) {
                player.sendMessage(Messages.ALREADY_IN_MAIN_LOBBY.asComponent());
                return;
            }
            player.setInstance(mainLobbyContainer).thenRun(() -> player.sendMessage(Messages.TELEPORT_TO_MAIN_LOBBY.asComponent()))
                    .exceptionally(ex -> {
                        player.sendMessage(Messages.FAILED_TELEPORT_MAIN_LOBBY.asComponent());
                        return null;
                    });
            return;
        }

        final Lobby lobby = lobbies.get(lobbyID);

        if (lobby == null) {
            player.sendMessage(Messages.LOBBY_NOT_FOUND.asComponent());
            return;
        }

        if (player.getInstance() == lobby.sharedInstance()) {
            player.sendMessage(Messages.ALREADY_IN_LOBBY.asComponent());
            return;
        }

        player.setInstance(lobby.sharedInstance())
                .thenRun(() -> {
                    player.setTag(TagConstants.PLAYER_LOBBY_ID_TAG, lobbyID);
                    player.sendMessage(Messages.TELEPORT_TO_LOBBY
                            .addPlaceholder("lobby", lobby.name())
                            .asComponent());
                })
                .exceptionally(ex -> {
                    player.sendMessage(Messages.FAILED_LOBBY_TELEPORT.asComponent());
                    return null;
                });
    }

    /**
     * Lists all available lobbies.
     *
     * @return a collection of lobby ids.
     */
    public @NotNull Collection<Integer> getAllLobbiesID() { return new ArrayList<>(lobbies.keySet()); }

    /**
     * Gets the lobby where the player currently is.
     *
     * @param player The player whose lobby needs to be found.
     * @return The Lobby where the player is, or null if not in a lobby.
     */
    public Lobby getLobbyByPlayer(@NotNull SlimePlayer player) {
        Integer lobbyID = player.getTag(TagConstants.PLAYER_LOBBY_ID_TAG);

        if (lobbyID == null) {
            return null;
        }

        return lobbies.get(lobbyID);
    }

    public Collection<Lobby> getAllLobbies() { return lobbies.values(); }

}