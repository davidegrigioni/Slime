package cc.davyy.slime.listeners;

import cc.davyy.slime.gui.ServerGUI;
import cc.davyy.slime.managers.LobbyManager;
import cc.davyy.slime.managers.SidebarManager;
import cc.davyy.slime.managers.SpawnManager;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.PosUtils;
import cc.davyy.slime.utils.TagConstants;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.server.common.ServerLinksPacket;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.FileUtils.getConfig;
import static cc.davyy.slime.utils.JoinUtils.applyJoinKit;

@Singleton
public class EventsListener {

    private static final ComponentLogger LOGGER = ComponentLogger.logger("EventsListener: ");
    private final EventNode<PlayerEvent> PLAYER_NODE;

    private final LobbyManager lobbyManager;
    private final SidebarManager sidebarManager;
    private final SpawnManager spawnManager;

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
                .addListener(PlayerDisconnectEvent.class, event -> sidebarManager.removeSidebar(event.getPlayer()))
                .addListener(PlayerSpawnEvent.class, event -> {
                    final SlimePlayer player = (SlimePlayer) event.getPlayer();
                    sendHeaderFooter(player);
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
                    if ("lobbysl".equals(item.getTag(Tag.String("action")))) {
                        new ServerGUI().open(player);
                    }
                })
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
        handler.addChild(PLAYER_NODE);
    }

}