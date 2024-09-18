package cc.davyy.slime.listeners;

import cc.davyy.slime.gui.LobbyGUI;
import cc.davyy.slime.gui.ServerGUI;
import cc.davyy.slime.managers.LobbyManager;
import cc.davyy.slime.managers.SidebarManager;
import cc.davyy.slime.managers.SpawnManager;
import cc.davyy.slime.model.Lobby;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.PosUtils;
import cc.davyy.slime.constants.TagConstants;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.MinestomAdventure;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.event.server.ServerTickMonitorEvent;
import net.minestom.server.event.trait.InventoryEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.monitoring.BenchmarkManager;
import net.minestom.server.monitoring.TickMonitor;
import net.minestom.server.network.packet.server.common.ServerLinksPacket;
import net.minestom.server.utils.MathUtils;
import net.minestom.server.utils.time.TimeUnit;
import net.minestom.server.utils.validate.Check;
import net.minestom.server.world.DimensionType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.FileUtils.getConfig;
import static cc.davyy.slime.utils.JoinUtils.applyJoinKit;

@Singleton
public class EventsListener {

    private static final ComponentLogger LOGGER = ComponentLogger.logger("EventsListener: ");
    private static final String LOBBY_SL = "lobbysl";
    private static final String SERVER_SL = "serversl";

    private final EventNode<PlayerEvent> playerNode;
    private final EventNode<InventoryEvent> inventoryNode;

    private final LobbyManager lobbyManager;
    private final SidebarManager sidebarManager;
    private final SpawnManager spawnManager;

    @Inject
    private Provider<LobbyGUI> lobbyGUIProvider;
    @Inject
    private Provider<ServerGUI> serverGUIProvider;

    private Instance limboInstance;

    private final AtomicReference<TickMonitor> lastTickMonitor = new AtomicReference<>();

    @Inject
    public EventsListener(LobbyManager lobbyManager, SidebarManager sidebarManager, SpawnManager spawnManager) {
        this.lobbyManager = lobbyManager;
        this.sidebarManager = sidebarManager;
        this.spawnManager = spawnManager;
        this.playerNode = createPlayerNode();
        this.inventoryNode = createInventoryNode();
    }

    public void init() {
        MinecraftServer.getGlobalEventHandler()
                .addListener(ServerTickMonitorEvent.class, event -> lastTickMonitor.set(event.getTickMonitor()))
                .addChild(inventoryNode)
                .addChild(playerNode);

        MinestomAdventure.AUTOMATIC_COMPONENT_TRANSLATION = true;
        MinestomAdventure.COMPONENT_TRANSLATOR = (c, l) -> c;
    }

    private EventNode<InventoryEvent> createInventoryNode() {
        return EventNode.type("inventory-node", EventFilter.INVENTORY)
                .addListener(InventoryPreClickEvent.class, event -> event.setCancelled(true))
                .addListener(InventoryClickEvent.class, event -> {
                    if (event.getInventory() instanceof LobbyGUI) {
                        LOGGER.debug("InventoryClickEvent triggered");
                        final SlimePlayer player = (SlimePlayer) event.getPlayer();
                        final ItemStack clickedItem = event.getClickedItem();
                        final Collection<Lobby> lobbies = lobbyManager.getAllLobbies();
                        final Integer lobbyTag = clickedItem.getTag(TagConstants.LOBBY_ID_TAG);

                        // Debug logs
                        LOGGER.debug("Player {} clicked item: {}", player.getName(), clickedItem);
                        if (lobbyTag != null) {
                            LOGGER.debug("Item has lobby tag: {}", lobbyTag);
                        } else {
                            LOGGER.debug("Item does not have a lobby tag.");
                        }

                        if (lobbyTag != null) {
                            Lobby selectedLobby = lobbies.stream()
                                    .filter(l -> l.id() == lobbyTag)
                                    .findFirst()
                                    .orElse(null);

                            if (selectedLobby != null) {
                                LOGGER.debug("Teleporting player {} to lobby with ID: {}", player.getName(), lobbyTag);
                                lobbyManager.teleportPlayerToLobby(player, lobbyTag);
                            } else {
                                LOGGER.debug("No lobby found with ID: {}", lobbyTag);
                            }
                        } else {
                            LOGGER.debug("Lobby tag was null. No action taken.");
                        }
                    }
                });
    }

    private EventNode<PlayerEvent> createPlayerNode() {
        return EventNode.type("player-node", EventFilter.PLAYER)
                .addListener(PlayerChatEvent.class, this::handleChatEvent)
                .addListener(PlayerMoveEvent.class, this::handlePlayerMoveEvent)
                .addListener(PlayerDisconnectEvent.class, this::handlePlayerDisconnectEvent)
                .addListener(PlayerSpawnEvent.class, this::handlePlayerSpawnEvent)
                .addListener(AsyncPlayerConfigurationEvent.class, this::handlePlayerConfigEvent)
                .addListener(PlayerUseItemEvent.class, this::handleItemUseEvent)
                .addListener(ItemDropEvent.class, event -> event.setCancelled(true))
                .addListener(PlayerSwapItemEvent.class, event -> event.setCancelled(true))
                .addListener(PlayerBlockBreakEvent.class, this::handleBlockBreakEvent);
    }

    private void handleChatEvent(PlayerChatEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();
        final String message = event.getMessage();
        final Instance instance = player.getInstance();

        if (instance.equals(limboInstance)) {
            event.setCancelled(true);
            player.sendMessage("You can't write in limbo");
            return;
        }

        instance.getPlayers().forEach(players -> {
            if (players instanceof SlimePlayer) {
                final Component formattedMessage = player.getChatFormat(message);

                event.setChatFormat(chatEvent -> formattedMessage);
            }
        });
    }

    private void handlePlayerMoveEvent(PlayerMoveEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();
        final int deathY = player.getDeathY();

        if (limboInstance != null && player.getInstance().equals(limboInstance)) {
            return;
        }

        if (player.getPosition().y() < deathY) {
            spawnManager.teleportToSpawn(player);
        }
    }

    private void handlePlayerDisconnectEvent(PlayerDisconnectEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();
        sidebarManager.removeSidebar(player);
    }

    private void handlePlayerSpawnEvent(PlayerSpawnEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();
        setupPlayerMetricsDisplay();
        sidebarManager.showSidebar(player);
        applyJoinKit(player);
        createServerLinks(player);
    }

    private void setupPlayerMetricsDisplay() {
        BenchmarkManager benchmarkManager = MinecraftServer.getBenchmarkManager();
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (lastTickMonitor.get() == null || MinecraftServer.getConnectionManager().getOnlinePlayerCount() == 0)
                return;

            long ramUsage = benchmarkManager.getUsedMemory() / (long) 1e6; // bytes to MB
            TickMonitor tickMonitor = lastTickMonitor.get();
            final Component header = Component.text("RAM USAGE: " + ramUsage + " MB")
                    .append(Component.newline())
                    .append(Component.text("TICK TIME: " + MathUtils.round(tickMonitor.getTickTime(), 2) + "ms"))
                    .append(Component.newline())
                    .append(Component.text("ACQ TIME: " + MathUtils.round(tickMonitor.getAcquisitionTime(), 2) + "ms"));

            final Component footer = benchmarkManager.getCpuMonitoringMessage();
            Audiences.players().sendPlayerListHeaderAndFooter(header, footer);
        }).repeat(10, TimeUnit.SERVER_TICK).schedule();
    }

    private void handlePlayerConfigEvent(AsyncPlayerConfigurationEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();
        final String posString = getConfig().getString("spawn.position");
        final Pos pos = PosUtils.fromString(posString);

        player.setLobbyID(10000);

        limboInstance = MinecraftServer.getInstanceManager().createInstanceContainer(DimensionType.THE_END);

        event.setSpawningInstance(limboInstance);

        Check.notNull(pos, "Position cannot be null, Check your Config!");
        player.setRespawnPoint(pos);

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            final Instance mainLobby = lobbyManager.getMainLobbyContainer();
            player.setInstance(mainLobby);

            MinecraftServer.getSchedulerManager().buildTask(() -> {
                if (limboInstance.getPlayers().isEmpty()) {
                    MinecraftServer.getInstanceManager().unregisterInstance(limboInstance);
                    return;
                }

                LOGGER.warn("Cannot unregister limbo instance {} as it still has players.", limboInstance.getUniqueId());
            }).delay(10, TimeUnit.SECOND).schedule();
        }).delay(10, TimeUnit.SECOND).schedule();
    }

    private void handleItemUseEvent(PlayerUseItemEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();
        final ItemStack item = event.getItemStack();
        switch (item.getTag(TagConstants.ACTION_TAG)) {
            case LOBBY_SL -> lobbyGUIProvider.get().open(player);
            case SERVER_SL -> serverGUIProvider.get().open(player);
            default -> {}
        }
    }

    private void handleBlockBreakEvent(PlayerBlockBreakEvent event) {
        final boolean blockBreakEnabled = getConfig().getBoolean("protection.disable-build-protection");
        final boolean messageEnabled = getConfig().getBoolean("protection.block-break-message.enable");

        if (blockBreakEnabled) {
            event.setCancelled(true);
            if (messageEnabled) {
                final String message = getConfig().getString("protection.block-break-message.message");
                if (message != null && !message.isEmpty()) {
                    event.getPlayer().sendMessage(of(message).build());
                    return;
                }
                LOGGER.warn("Block break message is not configured properly.");
            }
        }
    }

    private void createServerLinks(@NotNull SlimePlayer player) {
        final String newsLink = getConfig().getString("news-link");
        final String bugsReportLink = getConfig().getString("bugs-report-link");
        final String announcementLink = getConfig().getString("announcement-link");

        player.sendPacket(new ServerLinksPacket(
                new ServerLinksPacket.Entry(ServerLinksPacket.KnownLinkType.NEWS, newsLink),
                new ServerLinksPacket.Entry(ServerLinksPacket.KnownLinkType.ANNOUNCEMENTS, announcementLink),
                new ServerLinksPacket.Entry(ServerLinksPacket.KnownLinkType.BUG_REPORT, bugsReportLink)
        ));
    }

    private void sendHeaderFooter(@NotNull SlimePlayer player) {
        final String header = getConfig().getString("header");
        final String footer = getConfig().getString("footer");

        player.sendPlayerListHeaderAndFooter(
                of(header).build(),
                of(footer).build()
        );
    }

}