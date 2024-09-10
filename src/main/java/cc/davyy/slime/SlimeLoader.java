package cc.davyy.slime;

import cc.davyy.slime.commands.*;
import cc.davyy.slime.listeners.*;
import cc.davyy.slime.managers.*;
import cc.davyy.slime.module.SlimeModule;
import cc.davyy.slime.utils.ConsoleUtils;
import com.asintoto.minestomacr.MinestomACR;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.velocity.VelocityProxy;

import java.util.Optional;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.FileUtils.*;
import static cc.davyy.slime.utils.FileUtils.getConfig;
import static cc.davyy.slime.utils.GeneralUtils.getOnlineSlimePlayers;

public class SlimeLoader {

    private static final ComponentLogger LOGGER = ComponentLogger.logger("SlimeLogger");

    @Inject private BroadcastManager broadcastManager;
    @Inject private BrandManager brandManager;
    @Inject private LobbyManager lobbyManager;
    @Inject private SidebarManager sidebarManager;
    @Inject private HologramManager hologramManager;
    @Inject private SpawnManager spawnManager;
    @Inject private GameModeManager gameModeManager;

    @Inject private BroadCastCommand broadcastCommand;
    @Inject private DebugCommand debugCommand;
    @Inject private GameModeCommand gameModeCommand;
    @Inject private HologramCommand hologramCommand;
    @Inject private LobbyCommand lobbyCommand;
    @Inject private NPCCommand npcCommand;
    @Inject private SpawnCommand spawnCommand;

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

        //handleVelocityProxy();

        startServer(minecraftServer);
    }

    private void registerListeners() {
        final var handler = MinecraftServer.getGlobalEventHandler();

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
        new PlayerSpawnListener(sidebarManager).init(handler);
        new PlayerMoveListener(spawnManager).init(handler);
    }

    private void injectGuice() {
        LOGGER.info("Injecting dependencies using Guice...");
        final Injector injector = Guice.createInjector(new SlimeModule(this));
        injector.injectMembers(this);
    }

    private void registerCommands() {
        final var commandManager = MinecraftServer.getCommandManager();
        commandManager.register(broadcastCommand);
        commandManager.register(debugCommand);
        commandManager.register(gameModeCommand);
        commandManager.register(hologramCommand);
        commandManager.register(lobbyCommand);
        commandManager.register(npcCommand);
        commandManager.register(spawnCommand);
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

    private void validateConfig() {
        final String ip = getConfig().getString("network.ip");
        final int port = getConfig().getInt("network.port");
        if (ip == null || port == 0) {
            LOGGER.error("Invalid IP or port configuration.");
            throw new IllegalArgumentException("Invalid IP or port configuration.");
        }
    }

    private void handleVelocityProxy() {
        final String velocitySecret = getConfig().getString("velocity-secret");
        Optional.ofNullable(velocitySecret)
                .ifPresentOrElse(
                        secret -> {
                            VelocityProxy.enable(secret);
                            LOGGER.info("Enabled Velocity Support with provided secret.");
                        },
                        () -> LOGGER.info("Velocity support not enabled. No secret provided.")
                );
    }

}