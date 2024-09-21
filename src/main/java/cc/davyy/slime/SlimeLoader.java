package cc.davyy.slime;

import cc.davyy.slime.commands.ItemDisplayCommand;
import cc.davyy.slime.commands.VehicleCommand;
import cc.davyy.slime.commands.admin.*;
import cc.davyy.slime.commands.cosmetic.CosmeticCommand;
import cc.davyy.slime.commands.entities.HologramCommand;
import cc.davyy.slime.commands.entities.NPCCommand;
import cc.davyy.slime.commands.player.SidebarCommand;
import cc.davyy.slime.commands.player.SocialCommand;
import cc.davyy.slime.commands.player.SpawnCommand;
import cc.davyy.slime.commands.player.TeleportCommand;
import cc.davyy.slime.listeners.*;
import cc.davyy.slime.managers.*;
import cc.davyy.slime.managers.entities.HologramManager;
import cc.davyy.slime.model.ServerMode;
import cc.davyy.slime.module.SlimeModule;
import cc.davyy.slime.utils.ConsoleUtils;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minestom.server.MinecraftServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.FileUtils.*;
import static cc.davyy.slime.utils.FileUtils.getConfig;
import static cc.davyy.slime.utils.GeneralUtils.getOnlineSlimePlayers;

public class SlimeLoader {

    private static final ComponentLogger LOGGER = ComponentLogger.logger("SlimeLogger");

    private ServerMode serverMode;

    @Inject private BroadcastManager broadcastManager;
    @Inject private BrandManager brandManager;
    @Inject private LobbyManager lobbyManager;
    @Inject private SidebarManager sidebarManager;
    @Inject private HologramManager hologramManager;
    @Inject private SpawnManager spawnManager;
    @Inject private GameModeManager gameModeManager;

    @Inject private BroadCastCommand broadcastCommand;
    @Inject private ConfigReloadCommand configReloadCommand;
    @Inject private CosmeticCommand cosmeticCommand;
    @Inject private ExecuteCommand executeCommand;
    @Inject private GameModeCommand gameModeCommand;
    @Inject private HologramCommand hologramCommand;
    @Inject private LobbyCommand lobbyCommand;
    @Inject private NPCCommand npcCommand;
    @Inject private SayCommand sayCommand;
    @Inject private SocialCommand socialCommand;
    @Inject private SpawnCommand spawnCommand;
    @Inject private StopCommand stopCommand;
    @Inject private TeleportCommand teleportCommand;
    @Inject private SidebarCommand sidebarCommand;
    @Inject private VehicleCommand vehicleCommand;

    @Inject private EventsListener eventsListener;

    public void start() {
        LOGGER.info("Initializing Minecraft Server...");
        final MinecraftServer minecraftServer = MinecraftServer.init();

        setupFiles();
        injectGuice();

        LOGGER.info("Registering listeners...");
        eventsListener.init();

        LOGGER.info("Setting up console...");
        ConsoleUtils.setupConsole();

        LOGGER.info("Registering commands...");
        registerCommands();

        LOGGER.info("Setting up shutdown tasks...");
        setupShutdownTask();

        serverMode = getServerModeFromConfig();
        final String velocitySecret = (serverMode == ServerMode.VELOCITY) ? loadVelocitySecret() : "";
        serverMode.initEncryption(velocitySecret);

        startServer(minecraftServer);
    }

    private void injectGuice() {
        LOGGER.info("Injecting dependencies using Guice...");
        final Injector injector = Guice.createInjector(new SlimeModule());
        injector.injectMembers(this);
    }

    private void registerCommands() {
        final var commandManager = MinecraftServer.getCommandManager();

        commandManager.register(broadcastCommand,
                configReloadCommand,
                cosmeticCommand,
                executeCommand,
                gameModeCommand,
                hologramCommand,
                lobbyCommand,
                npcCommand,
                sayCommand,
                socialCommand,
                spawnCommand,
                stopCommand,
                sidebarCommand,
                vehicleCommand,
                new ItemDisplayCommand(),
                teleportCommand);
    }

    private void setupShutdownTask() {
        MinecraftServer.getSchedulerManager().buildShutdownTask(() -> {
            final String kickMessage = getMessages().getString("messages.kick");
            getOnlineSlimePlayers().forEach(player -> player.kick(of(kickMessage).build()));
            //MinecraftServer.getInstanceManager().getInstances().forEach(Instance::saveChunksToStorage);
        });
    }

    private void startServer(MinecraftServer minecraftServer) {
        final String ip = getConfig().getString("network.ip");
        final int port = getConfig().getInt("network.port");
        minecraftServer.start(ip, port);
        LOGGER.info("Server started on IP: {}, Port: {}", ip, port);
    }

    private ServerMode getServerModeFromConfig() {
        final String modeString = getConfig().getString("network.type").toUpperCase();
        try {
            return ServerMode.valueOf(modeString);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Invalid server mode in config ({}), defaulting to ONLINE mode.", modeString);
            return ServerMode.ONLINE;
        }
    }

    private String loadVelocitySecret() {
        final Path path = Paths.get("./configs/forwarding.secret");
        try {
            return Files.readString(path).trim();
        } catch (IOException ex) {
            LOGGER.error("Failed to read velocity secret from \"{}\". Velocity is enabled in config, aborting start.", path);
            throw new RuntimeException("Could not load Velocity secret", ex);
        }
    }

}