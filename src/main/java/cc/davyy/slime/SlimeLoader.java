package cc.davyy.slime;

import cc.davyy.slime.commands.DebugCommand;
import cc.davyy.slime.commands.LobbyCommand;
import cc.davyy.slime.commands.RegionCommand;
import cc.davyy.slime.listeners.*;
import cc.davyy.slime.managers.BrandManager;
import cc.davyy.slime.managers.LobbyManager;
import cc.davyy.slime.managers.RegionManager;
import cc.davyy.slime.module.SlimeModule;
import cc.davyy.slime.utils.ConsoleUtils;
import com.asintoto.minestomacr.MinestomACR;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minestom.server.MinecraftServer;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.ColorUtils.txt;
import static cc.davyy.slime.utils.FileUtils.*;
import static cc.davyy.slime.utils.FileUtils.getConfig;

public class SlimeLoader {

    private final ComponentLogger componentLogger = ComponentLogger.logger(SlimeLoader.class);

    private BrandManager brandManager;
    private RegionManager regionManager;
    private LobbyManager lobbyManager;

    public void start() {
        MinecraftServer minecraftServer = MinecraftServer.init();

        setupFiles();

        injectGuice();

        registerListeners();

        ConsoleUtils.setupConsole();

        MinestomACR.init();

        MinecraftServer.getCommandManager().register(new RegionCommand(regionManager));
        MinecraftServer.getCommandManager().register(new LobbyCommand(lobbyManager));
        MinecraftServer.getCommandManager().register(new DebugCommand(lobbyManager));

        MinecraftServer.getSchedulerManager().buildShutdownTask(() -> {
            var onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers();
            String kickMessage = getMessages().getString("messages.kick");
            onlinePlayers.forEach(player -> player.kick(of(kickMessage)
                    .build()));
            componentLogger.info(txt("Server Closing..."));
            //MinecraftServer.getInstanceManager().getInstances().forEach(Instance::saveChunksToStorage);
        });

        String ip = getConfig().getString("network.ip");
        int port = getConfig().getInt("network.port");
        minecraftServer.start(ip, port);
    }

    private void registerListeners() {
        var handler = MinecraftServer.getGlobalEventHandler();
        handler.addListener(new PlayerSpawnListener());
        new AsyncPlayerConfigurationListener(lobbyManager).init(handler);
        new RegionListener(regionManager).init(handler);
    }

    private void injectGuice() {
        Injector injector = Guice.createInjector(new SlimeModule(this));

        regionManager = injector.getInstance(RegionManager.class);
        lobbyManager = injector.getInstance(LobbyManager.class);
        brandManager = injector.getInstance(BrandManager.class);
    }

}