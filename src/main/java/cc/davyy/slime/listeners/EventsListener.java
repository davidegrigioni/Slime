package cc.davyy.slime.listeners;

import cc.davyy.slime.gui.LobbyGUI;
import cc.davyy.slime.gui.ServerGUI;
import cc.davyy.slime.managers.LobbyManager;
import cc.davyy.slime.managers.SidebarManager;
import cc.davyy.slime.managers.SpawnManager;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.PosUtils;
import cc.davyy.slime.misc.TagConstants;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.MinestomAdventure;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.event.server.ServerTickMonitorEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.monitoring.BenchmarkManager;
import net.minestom.server.monitoring.TickMonitor;
import net.minestom.server.network.packet.server.common.ServerLinksPacket;
import net.minestom.server.utils.MathUtils;
import net.minestom.server.utils.time.TimeUnit;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.FileUtils.getConfig;
import static cc.davyy.slime.utils.JoinUtils.applyJoinKit;

@Singleton
public class EventsListener {

    private static final ComponentLogger LOGGER = ComponentLogger.logger("EventsListener: ");
    private static final String LOBBY_SL = "lobbysl";
    private static final String SERVER_SL = "serversl";

    private final EventNode<PlayerEvent> PLAYER_NODE;

    private final LobbyManager lobbyManager;
    private final SidebarManager sidebarManager;
    private final SpawnManager spawnManager;

    @Inject
    private Provider<LobbyGUI> lobbyGUIProvider;
    @Inject
    private Provider<ServerGUI> serverGUIProvider;

    private final AtomicReference<TickMonitor> LAST_TICK = new AtomicReference<>();

    @Inject
    public EventsListener(LobbyManager lobbyManager, SidebarManager sidebarManager, SpawnManager spawnManager) {
        this.lobbyManager = lobbyManager;
        this.sidebarManager = sidebarManager;
        this.spawnManager = spawnManager;
        this.PLAYER_NODE = createPlayerNode();
    }

    private EventNode<PlayerEvent> createPlayerNode() {
        return EventNode.type("player-node", EventFilter.PLAYER)
                .addListener(PlayerChatEvent.class, event -> {
                    final SlimePlayer player = (SlimePlayer) event.getPlayer();
                    event.setChatFormat(chatEvent -> player.getChatFormat(chatEvent.getMessage()));
                })
                .addListener(PlayerMoveEvent.class, event -> {
                    final SlimePlayer player = (SlimePlayer) event.getPlayer();
                    final int deathY = player.getInstance().getTag(TagConstants.DEATH_Y);
                    if (player.getPosition().y() < deathY) {
                        spawnManager.teleportToSpawn(player); // No issue with spawnManager now
                    }
                })
                .addListener(PlayerDisconnectEvent.class, event -> sidebarManager.removeSidebar((SlimePlayer) event.getPlayer()))
                .addListener(PlayerSpawnEvent.class, event -> {
                    final SlimePlayer player = (SlimePlayer) event.getPlayer();

                    //sendHeaderFooter(player);

                    BenchmarkManager benchmarkManager = MinecraftServer.getBenchmarkManager();
                    MinecraftServer.getSchedulerManager().buildTask(() -> {
                        if (LAST_TICK.get() == null || MinecraftServer.getConnectionManager().getOnlinePlayerCount() == 0)
                            return;

                        long ramUsage = benchmarkManager.getUsedMemory();
                        ramUsage /= (long) 1e6; // bytes to MB

                        TickMonitor tickMonitor = LAST_TICK.get();
                        final Component header = Component.text("RAM USAGE: " + ramUsage + " MB")
                                .append(Component.newline())
                                .append(Component.text("TICK TIME: " + MathUtils.round(tickMonitor.getTickTime(), 2) + "ms"))
                                .append(Component.newline())
                                .append(Component.text("ACQ TIME: " + MathUtils.round(tickMonitor.getAcquisitionTime(), 2) + "ms"));
                        final Component footer = benchmarkManager.getCpuMonitoringMessage();
                        Audiences.players().sendPlayerListHeaderAndFooter(header, footer);
                    }).repeat(10, TimeUnit.SERVER_TICK).schedule();

                    createServerLinks(player);

                    sidebarManager.showSidebar(player);

                    applyJoinKit(player);
                })
                .addListener(InventoryPreClickEvent.class, event -> event.setCancelled(true))
                .addListener(AsyncPlayerConfigurationEvent.class, event -> {
                    final Player player = event.getPlayer();
                    final String posString = getConfig().getString("spawn.position");
                    final Pos pos = PosUtils.fromString(posString);

                    event.setSpawningInstance(lobbyManager.getMainLobbyContainer());
                    Check.notNull(pos, "Position cannot be null, Check your Config!");
                    player.setRespawnPoint(pos);
                })
                .addListener(PlayerUseItemEvent.class, event -> {
                    final SlimePlayer player = (SlimePlayer) event.getPlayer();
                    final ItemStack item = event.getItemStack();

                    switch (item.getTag(TagConstants.ACTION_TAG)) {
                        case LOBBY_SL -> {
                            LobbyGUI lobbyGUI = lobbyGUIProvider.get();
                            lobbyGUI.open(player);
                        }
                        case SERVER_SL -> {
                            ServerGUI serverGUI = serverGUIProvider.get();
                            serverGUI.open(player);
                        }
                        default -> {}
                    }
                })
                .addListener(ItemDropEvent.class, event -> event.setCancelled(true))
                .addListener(PlayerSwapItemEvent.class, event -> event.setCancelled(true))
                .addListener(PlayerBlockBreakEvent.class, event -> {
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
                });
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

    public void init() {
        var handler = MinecraftServer.getGlobalEventHandler();
        handler.addListener(ServerTickMonitorEvent.class, event -> LAST_TICK.set(event.getTickMonitor()));
        handler.addChild(PLAYER_NODE);

        MinestomAdventure.AUTOMATIC_COMPONENT_TRANSLATION = true;
        MinestomAdventure.COMPONENT_TRANSLATOR = (c, l) -> c;
    }

}