package cc.davyy.slime.listeners;

import cc.davyy.slime.gui.LobbyGUI;
import cc.davyy.slime.gui.ServerGUI;
import cc.davyy.slime.managers.LobbyManager;
import cc.davyy.slime.managers.SidebarManager;
import cc.davyy.slime.managers.SpawnManager;
import cc.davyy.slime.managers.entities.nametag.NameTag;
import cc.davyy.slime.managers.entities.nametag.NameTagManager;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.FileUtils;
import cc.davyy.slime.utils.Messages;
import cc.davyy.slime.utils.PosUtils;
import cc.davyy.slime.constants.TagConstants;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.MinestomAdventure;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientSteerVehiclePacket;
import net.minestom.server.network.packet.server.common.ServerLinksPacket;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.JoinUtils.*;

@Singleton
public class EventsListener {

    private static final ComponentLogger LOGGER = ComponentLogger.logger("EventsListener");
    private static final String LOBBY_SL = "lobbysl";
    private static final String SERVER_SL = "serversl";
    private static final String SHOW = "show";
    private static final String HIDE = "hide";

    private final EventNode<PlayerEvent> playerNode;

    private final LobbyManager lobbyManager;
    private final SidebarManager sidebarManager;
    private final SpawnManager spawnManager;
    private final NameTagManager nameTagManager;

    @Inject
    private Provider<LobbyGUI> lobbyGUIProvider;
    @Inject
    private Provider<ServerGUI> serverGUIProvider;

    @Inject
    public EventsListener(LobbyManager lobbyManager, SidebarManager sidebarManager, SpawnManager spawnManager, NameTagManager nameTagManager) {
        this.lobbyManager = lobbyManager;
        this.sidebarManager = sidebarManager;
        this.spawnManager = spawnManager;
        this.nameTagManager = nameTagManager;
        this.playerNode = createPlayerNode();
    }

    public void init() {
        MinecraftServer.getGlobalEventHandler()
                .addChild(playerNode);

        MinestomAdventure.AUTOMATIC_COMPONENT_TRANSLATION = true;
        MinestomAdventure.COMPONENT_TRANSLATOR = (c, l) -> c;
    }

    private EventNode<PlayerEvent> createPlayerNode() {
        return EventNode.type("player-node", EventFilter.PLAYER)
                .addListener(InventoryPreClickEvent.class, event -> {
                    if (event.getInventory() instanceof ServerGUI || event.getInventory() instanceof LobbyGUI) return;
                    event.setCancelled(true);
                })
                .addListener(PlayerEntityInteractEvent.class, this::playerInteract)
                .addListener(PlayerPacketEvent.class, this::playerPacket)
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

    private void playerInteract(@NotNull PlayerEntityInteractEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();
        final Entity target = event.getTarget();

        if (event.getHand() != Player.Hand.MAIN) return;

        target.addPassenger(player);
    }

    private void playerPacket(@NotNull PlayerPacketEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();
        final ClientPacket clientPacket = event.getPacket();

        if (clientPacket instanceof ClientSteerVehiclePacket clientSteerVehiclePacket) {
            final Entity vehicle = player.getVehicle();
            final float fwSpeed = clientSteerVehiclePacket.forward();
            final float swSpeed = clientSteerVehiclePacket.sideways();
            final byte flags = clientSteerVehiclePacket.flags();

            if (vehicle != null) {
                final Pos pos = player.getPosition();

                vehicle.setView(pos.yaw(), pos.pitch());

                final Vec forwardDir = pos.direction();
                final Vec sideways = forwardDir.cross(new Vec(0, -1, 0));
                final Vec total = forwardDir.mul(fwSpeed).add(sideways.mul(swSpeed));

                vehicle.setVelocity(vehicle.getVelocity().add(total));

                switch (flags) {
                    case 0x1 -> {}
                    case 0x2 -> vehicle.removePassenger(player);
                }
            }
        }
    }

    private void handleChatEvent(PlayerChatEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();
        final String message = event.getMessage();
        final Instance instance = player.getInstance();

        final Component formattedMessage;
        if (player.hasPermission("slime.colorchat")) {
            formattedMessage = player.getChatFormat(message);
        } else {
            formattedMessage = player.getDefaultChatFormat(message);
        }

        event.setCancelled(true);

        instance.getPlayers().forEach(players -> {
            if (players instanceof SlimePlayer slimePlayer) {
                slimePlayer.sendMessage(formattedMessage);
            }
        });

    }

    private void handlePlayerMoveEvent(PlayerMoveEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();
        final Instance playerInstance = player.getInstance();

        final Integer deathY = playerInstance.getTag(TagConstants.DEATH_Y);

        if (deathY == null) {
            LOGGER.warn("Death Y value is missing for instance: {}", playerInstance.getUniqueId());
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

        sendHeaderFooter(player);

        setNameTags(player, player.getPrefix()
                .append(Component.text(" "))
                .append(player.getName()));

        sidebarManager.showSidebar(player);

        applyJoinKit(player);

        createServerLinks(player);
    }

    private void handlePlayerConfigEvent(AsyncPlayerConfigurationEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();
        final String posString = FileUtils.getConfig().getString("spawn.position");
        final Pos pos = PosUtils.fromString(posString);
        final Instance instance = lobbyManager.getMainLobbyContainer();

        if (!player.hasLobbyID()) {
            final int lobbyIDForInstance = lobbyManager.getLobbyIDForInstance(instance);
            player.setLobbyID(lobbyIDForInstance);
            event.setSpawningInstance(instance);
            return;
        }

        Check.notNull(pos, "Position cannot be null, Check your Config!");
        player.setRespawnPoint(pos);
    }

    private void handleItemUseEvent(PlayerUseItemEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();
        final ItemStack item = event.getItemStack();

        if (event.getHand() != Player.Hand.MAIN) return;

        switch (item.getTag(TagConstants.ACTION_TAG)) {
            case LOBBY_SL -> lobbyGUIProvider.get().open(player);
            case SERVER_SL -> serverGUIProvider.get().open(player);
            case SHOW -> {
                final ItemStack hideItem = createItemFromConfig("items.hide-item");
                final int hideSlot = FileUtils.getConfig().getInt("items.hide-item.slot");

                player.getInventory().setItemStack(hideSlot, hideItem);
                player.updateViewableRule(playerVisible -> true);
                player.sendMessage(Messages.SHOW
                        .asComponent());
            }
            case HIDE -> {
                final ItemStack showItem = createItemFromConfig("items.show-item");
                final int showSlot = FileUtils.getConfig().getInt("items.show-item.slot");

                player.getInventory().setItemStack(showSlot, showItem);
                player.updateViewableRule(playerVisible -> false);
                player.sendMessage(Messages.HIDE
                        .asComponent());
            }
            default -> {}
        }
    }

    private void handleBlockBreakEvent(PlayerBlockBreakEvent event) {
        final boolean blockBreakEnabled = FileUtils.getConfig().getBoolean("protection.disable-build-protection");
        final boolean messageEnabled = FileUtils.getConfig().getBoolean("protection.block-break-message.enable");

        if (blockBreakEnabled) {
            event.setCancelled(true);
            if (messageEnabled) {
                final String message = FileUtils.getConfig().getString("protection.block-break-message.message");
                if (message != null && !message.isEmpty()) {
                    event.getPlayer().sendMessage(of(message).build());
                    return;
                }
                LOGGER.warn("Block break message is not configured properly.");
            }
        }
    }

    @SuppressWarnings("all")
    private void createServerLinks(@NotNull SlimePlayer player) {
        final String newsLink = FileUtils.getConfig().getString("news-link");
        final String bugsReportLink = FileUtils.getConfig().getString("bugs-report-link");
        final String announcementLink = FileUtils.getConfig().getString("announcement-link");

        player.sendPacket(new ServerLinksPacket(
                new ServerLinksPacket.Entry(ServerLinksPacket.KnownLinkType.NEWS, newsLink),
                new ServerLinksPacket.Entry(ServerLinksPacket.KnownLinkType.ANNOUNCEMENTS, announcementLink),
                new ServerLinksPacket.Entry(ServerLinksPacket.KnownLinkType.BUG_REPORT, bugsReportLink)
        ));
    }

    private void sendHeaderFooter(@NotNull SlimePlayer player) {
        final List<String> headerList = FileUtils.getConfig().getStringList("header");
        final List<String> footerList = FileUtils.getConfig().getStringList("footer");

        final String header = String.join("\n", headerList);
        final String footer = String.join("\n", footerList);

        player.sendPlayerListHeaderAndFooter(
                of(header).build(),
                of(footer).build()
        );
    }

    private void setNameTags(@NotNull SlimePlayer player, @NotNull Component text) {
        final NameTag nameTag = nameTagManager.createNameTag(player);

        nameTag.setText(text);
        nameTag.addViewer(player);
        nameTag.mount();
    }

}