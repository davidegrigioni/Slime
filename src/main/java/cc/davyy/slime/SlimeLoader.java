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
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.scoreboard.TeamBuilder;
import net.minestom.server.scoreboard.TeamManager;

import java.util.Collection;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.FileUtils.*;
import static cc.davyy.slime.utils.FileUtils.getConfig;

public class SlimeLoader {

    private static final ComponentLogger LOGGER = ComponentLogger.logger(SlimeLoader.class);

    @Inject private BrandManager brandManager;
    @Inject private LobbyManager lobbyManager;
    @Inject private RegionManager regionManager;
    @Inject private SidebarManager sidebarManager;
    @Inject private HologramManager hologramManager;

    private NPCManager npcManager;
    private NameTagManager nameTagManager;

    public void start() {
        LOGGER.info("Initializing Minecraft Server...");
        MinecraftServer minecraftServer = MinecraftServer.init();

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
        var handler = MinecraftServer.getGlobalEventHandler();

        npcManager = new NPCManager(handler);
        TeamManager teamManager = MinecraftServer.getTeamManager();
        Team nameTagTeam = new TeamBuilder("name-tags", teamManager)
                .collisionRule(TeamsPacket.CollisionRule.NEVER)
                .build();
        nameTagManager = new NameTagManager(handler, entity -> nameTagTeam);

        new AsyncPlayerConfigurationListener(lobbyManager).init(handler);
        new InventoryListener().init(handler);
        new PlayerChatListener().init(handler);
        new PlayerSpawnListener(nameTagManager, sidebarManager).init(handler);
        new RegionListener(regionManager).init(handler);
    }

    private void injectGuice() {
        LOGGER.info("Injecting dependencies using Guice...");
        Injector injector = Guice.createInjector(new SlimeModule(this));
        injector.injectMembers(this);
    }

    private void registerCommands() {
        var commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new RegionCommand(regionManager));
        commandManager.register(new LobbyCommand(lobbyManager));
        commandManager.register(new DebugCommand(lobbyManager));
        commandManager.register(new NPCCommand(npcManager, nameTagManager));
        commandManager.register(new HologramCommand(hologramManager));
    }

    private void setupShutdownTask() {
        MinecraftServer.getSchedulerManager().buildShutdownTask(() -> {
            Collection<Player> onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers();
            String kickMessage = getMessages().getString("messages.kick");
            onlinePlayers.forEach(player -> player.kick(of(kickMessage).build()));
        });
    }

    private void startServer(MinecraftServer minecraftServer) {
        String ip = getConfig().getString("network.ip");
        int port = getConfig().getInt("network.port");
        minecraftServer.start(ip, port);
        LOGGER.info("Server started on IP: {}, Port: {}", ip, port);
    }

    private void validateConfig() {
        String ip = getConfig().getString("network.ip");
        int port = getConfig().getInt("network.port");
        if (ip == null || port == 0) {
            LOGGER.error("Invalid IP or port configuration.");
            throw new IllegalArgumentException("Invalid IP or port configuration.");
        }
    }

}