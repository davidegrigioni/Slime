package cc.davyy.slime;

import cc.davyy.slime.listeners.*;
import cc.davyy.slime.managers.*;
import cc.davyy.slime.managers.entities.HologramManager;
import cc.davyy.slime.model.ServerMode;
import cc.davyy.slime.module.SlimeModule;
import cc.davyy.slime.utils.ConsoleUtils;
import cc.davyy.slime.utils.Messages;
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
import static cc.davyy.slime.utils.GeneralUtils.getOnlineSlimePlayers;

public final class SlimeLoader {

    private static final ComponentLogger LOGGER = ComponentLogger.logger("SlimeLogger");

    private ServerMode serverMode;

    @Inject private ConfigManager configManager;
    @Inject private CommandManager commandManager;
    @Inject private BroadcastManager broadcastManager;
    @Inject private BrandManager brandManager;
    @Inject private LobbyManager lobbyManager;
    @Inject private SidebarManager sidebarManager;
    @Inject private HologramManager hologramManager;
    @Inject private SpawnManager spawnManager;
    @Inject private GameModeManager gameModeManager;

    @Inject private EventsListener eventsListener;

    public void init() {
        LOGGER.info("Initializing Minecraft Server...");
        final MinecraftServer minecraftServer = MinecraftServer.init();

        setupFiles();
        injectGuice();

        configManager.setup();
        Messages.setConfigManager(configManager);

        LOGGER.info("Registering listeners...");
        eventsListener.init();

        LOGGER.info("Setting up console...");
        ConsoleUtils.setupConsole();

        LOGGER.info("Registering commands...");
        commandManager.init();

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

    private void setupShutdownTask() {
        MinecraftServer.getSchedulerManager().buildShutdownTask(() -> {
            final String kickMessage = configManager.getMessages().getString("messages.kick");
            getOnlineSlimePlayers().forEach(player -> player.kick(of(kickMessage).build()));
            //MinecraftServer.getInstanceManager().getInstances().forEach(Instance::saveChunksToStorage);
        });
    }

    private void startServer(MinecraftServer minecraftServer) {
        final String ip = configManager.getConfig().getString("network.ip");
        final int port = configManager.getConfig().getInt("network.port");
        minecraftServer.start(ip, port);
        LOGGER.info("Server started on IP: {}, Port: {}", ip, port);
    }

    private ServerMode getServerModeFromConfig() {
        final String modeString = configManager.getConfig().getString("network.type").toUpperCase();
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