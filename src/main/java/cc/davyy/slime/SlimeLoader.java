package cc.davyy.slime;

import cc.davyy.slime.commands.*;
import cc.davyy.slime.listeners.*;
import cc.davyy.slime.managers.*;
import cc.davyy.slime.entities.NPCManager;
import cc.davyy.slime.managers.npc.NameTagManager;
import cc.davyy.slime.module.SlimeModule;
import cc.davyy.slime.utils.ConsoleUtils;
import com.asintoto.minestomacr.MinestomACR;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.scoreboard.TeamBuilder;
import net.minestom.server.scoreboard.TeamManager;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.FileUtils.*;
import static cc.davyy.slime.utils.FileUtils.getConfig;
import static cc.davyy.slime.utils.GeneralUtils.getOnlinePlayers;

public class SlimeLoader {

    private static final ComponentLogger LOGGER = ComponentLogger.logger(SlimeLoader.class);

    @Inject private BroadcastManager broadcastManager;
    @Inject private BrandManager brandManager;
    @Inject private LobbyManager lobbyManager;
    @Inject private RegionManager regionManager;
    @Inject private SidebarManager sidebarManager;
    @Inject private HologramManager hologramManager;
    @Inject private SpawnManager spawnManager;

    private NPCManager npcManager;
    private NameTagManager nameTagManager;

    public void start() {
        LOGGER.info("Initializing Minecraft Server...");
        final MinecraftServer minecraftServer = MinecraftServer.init();

        setupFiles();
        validateConfig();
        injectGuice();

        LOGGER.info("Registering listeners...");
        registerListeners();

        LOGGER.info("Setting up console...");
        ConsoleUtils.setupConsole();

        LOGGER.info("Initializing MinestomACR...");
        MinestomACR.init();

        LOGGER.info("Registering commands...");
        registerCommands();

        LOGGER.info("Setting up shutdown tasks...");
        setupShutdownTask();

        startServer(minecraftServer);
    }

    private void registerListeners() {
        final var handler = MinecraftServer.getGlobalEventHandler();

        npcManager = new NPCManager(handler);
        final TeamManager teamManager = MinecraftServer.getTeamManager();
        final Team nameTagTeam = new TeamBuilder("name-tags", teamManager)
                .collisionRule(TeamsPacket.CollisionRule.NEVER)
                .build();
        nameTagManager = new NameTagManager(handler, entity -> nameTagTeam);

        /*handler.addListener(PlayerFlagEvent.class, e ->
                e.player().sendMessage(Component
                        .text("You have been flagged for " + e.checkName() + " with a certainty of " + e.certainty())
                        .color(NamedTextColor.RED)));

        MangoAC.Config config = new MangoAC.Config(false, List.of(), List.of());
        MangoAC ac = new MangoAC(config);
        ac.start();*/

        new AsyncPlayerConfigurationListener(lobbyManager).init(handler);
        new InventoryListener().init(handler);
        new PlayerChatListener().init(handler);
        new PlayerSpawnListener(nameTagManager, sidebarManager).init(handler);
        new RegionListener(regionManager).init(handler);
    }

    private void injectGuice() {
        LOGGER.info("Injecting dependencies using Guice...");
        final Injector injector = Guice.createInjector(new SlimeModule(this));
        injector.injectMembers(this);
    }

    private void registerCommands() {
        final var commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new RegionCommand(regionManager));
        commandManager.register(new LobbyCommand(lobbyManager));
        commandManager.register(new DebugCommand(lobbyManager));
        commandManager.register(new NPCCommand(npcManager, nameTagManager));
        commandManager.register(new HologramCommand(hologramManager));
        commandManager.register(new BroadCastCommand(broadcastManager));
        commandManager.register(new SpawnCommand(spawnManager));
    }

    private void setupShutdownTask() {
        MinecraftServer.getSchedulerManager().buildShutdownTask(() -> {
            String kickMessage = getMessages().getString("messages.kick");
            getOnlinePlayers().forEach(player -> player.kick(of(kickMessage).build()));
            //MinecraftServer.getInstanceManager().getInstances().forEach(Instance::saveChunksToStorage);
        });
    }

    private void startServer(MinecraftServer minecraftServer) {
        final String ip = getConfig().getString("network.ip");
        final int port = getConfig().getInt("network.port");
        minecraftServer.start(ip, port);
        LOGGER.info("Server started on IP: {}, Port: {}", ip, port);
    }

    private void validateConfig() {
        final String ip = getConfig().getString("network.ip");
        final int port = getConfig().getInt("network.port");
        if (ip == null || port == 0) {
            LOGGER.error("Invalid IP or port configuration.");
            throw new IllegalArgumentException("Invalid IP or port configuration.");
        }
    }

}