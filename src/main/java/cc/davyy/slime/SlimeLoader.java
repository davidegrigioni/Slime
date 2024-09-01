package cc.davyy.slime;

import cc.davyy.slime.commands.RegionCommand;
import cc.davyy.slime.listeners.AsyncPlayerConfigurationListener;
import cc.davyy.slime.listeners.MOTDListener;
import cc.davyy.slime.listeners.PlayerSpawnListener;
import cc.davyy.slime.listeners.RegionListener;
import cc.davyy.slime.managers.RegionManager;
import cc.davyy.slime.misc.BrandAnimator;
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

public final class SlimeLoader {

    private final ComponentLogger componentLogger = ComponentLogger.logger(SlimeLoader.class);

    private RegionManager regionManager;
    private BrandAnimator animator;

    public void start() {
        final MinecraftServer minecraftServer = MinecraftServer.init();

        setupLuckPerms();
        setupConfig();

        registerListeners();

        ConsoleUtils.setupConsole();

        injectGuice();

        MinestomACR.init();

        MinecraftServer.getCommandManager().register(new RegionCommand(regionManager));

        MinecraftServer.getSchedulerManager().buildShutdownTask(() -> {
            final var onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers();
            final String kickMessage = getMessages().getString("kick");
            onlinePlayers.forEach(player -> player.kick(of(kickMessage)
                    .build()));
            componentLogger.info(txt("Server Closing..."));
        });

        final String ip = getConfig().getString("ip");
        final int port = getConfig().getInt("port");
        minecraftServer.start(ip, port);
    }

    private void registerListeners() {
        final var handler = MinecraftServer.getGlobalEventHandler();
        handler.addListener(new AsyncPlayerConfigurationListener());
        handler.addListener(new PlayerSpawnListener());
        handler.addListener(new MOTDListener());
        new RegionListener(regionManager).init(handler);
    }

    private void injectGuice() {
        final Injector injector = Guice.createInjector(new SlimeModule(this));

        animator = injector.getInstance(BrandAnimator.class);
        regionManager = injector.getInstance(RegionManager.class);
    }

}