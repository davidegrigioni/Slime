package cc.davyy.slime;

import cc.davyy.slime.commands.RegionCommand;
import cc.davyy.slime.listeners.AsyncPlayerConfigurationListener;
import cc.davyy.slime.listeners.BlockBreakListener;
import cc.davyy.slime.listeners.PlayerSpawnListener;
import cc.davyy.slime.listeners.RegionListener;
import cc.davyy.slime.managers.RegionManager;
import cc.davyy.slime.misc.BrandAnimator;
import cc.davyy.slime.module.SlimeModule;
import cc.davyy.slime.utils.ConsoleUtils;
import com.asintoto.minestomacr.MinestomACR;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.minestom.server.MinecraftServer;

import static cc.davyy.slime.utils.FileUtils.*;
import static cc.davyy.slime.utils.FileUtils.getConfig;

public final class SlimeLoader {

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

        final String ip = getConfig().getString("ip");
        final int port = getConfig().getInt("port");
        minecraftServer.start(ip, port);
    }

    private void registerListeners() {
        final var handler = MinecraftServer.getGlobalEventHandler();
        handler.addListener(new AsyncPlayerConfigurationListener());
        handler.addListener(new PlayerSpawnListener());
        handler.addListener(new BlockBreakListener(regionManager));
        handler.addListener(new RegionListener(regionManager));
    }

    private void injectGuice() {
        final Injector injector = Guice.createInjector(new SlimeModule(this));

        animator = injector.getInstance(BrandAnimator.class);
        regionManager = injector.getInstance(RegionManager.class);
    }

}