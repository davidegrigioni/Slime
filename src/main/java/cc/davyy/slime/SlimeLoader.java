package cc.davyy.slime;

import cc.davyy.slime.managers.entities.HologramManager;
import cc.davyy.slime.managers.entities.SidebarManager;
import cc.davyy.slime.managers.gameplay.*;
import cc.davyy.slime.managers.general.CommandManager;
import cc.davyy.slime.config.ConfigManager;
import cc.davyy.slime.managers.general.EventsManager;
import cc.davyy.slime.model.ServerMode;
import cc.davyy.slime.guice.SlimeModule;
import cc.davyy.slime.utils.ConsoleUtils;
import cc.davyy.slime.model.Messages;
import cc.davyy.slime.utils.ServerConfigUtils;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minestom.server.MinecraftServer;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.FileUtils.*;
import static cc.davyy.slime.utils.GeneralUtils.getOnlineSlimePlayers;

public final class SlimeLoader {

    private static final ComponentLogger LOGGER = ComponentLogger.logger(SlimeLoader.class);

    private ServerMode serverMode;

    @Inject private ConfigManager configManager;
    @Inject private CommandManager commandManager;
    @Inject private BroadcastManager broadcastManager;
    @Inject private BrandManager brandManager;
    @Inject private LobbyManager lobbyManager;
    @Inject private SidebarManager sidebarManager;
    @Inject private SpawnManager spawnManager;
    @Inject private GameModeManager gameModeManager;
    @Inject private HologramManager hologramManager;

    @Inject private EventsManager eventsManager;

    public void init() {
        LOGGER.info("Initializing Minecraft Server...");
        final MinecraftServer minecraftServer = MinecraftServer.init();

        setupFiles();
        injectGuice();
        configManager.setup();

        Messages.setConfigManager(configManager);

        LOGGER.info("Registering listeners...");
        eventsManager.init();

        LOGGER.info("Setting up console...");
        ConsoleUtils.setupConsole();

        LOGGER.info("Registering commands...");
        commandManager.init();

        LOGGER.info("Setting up shutdown tasks...");
        setupShutdownTask();

        serverMode = ServerConfigUtils.getServerModeFromConfig(configManager);
        final String velocitySecret = (serverMode == ServerMode.VELOCITY) ? ServerConfigUtils.loadVelocitySecret() : "";
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

}