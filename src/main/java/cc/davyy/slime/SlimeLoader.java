package cc.davyy.slime;

import cc.davyy.slime.listeners.APCListener;
import cc.davyy.slime.listeners.BBListener;
import cc.davyy.slime.listeners.PJListener;
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

    public void start() {
        final MinecraftServer minecraftServer = MinecraftServer.init();

        setupLuckPerms();
        setupConfig();

        registerListeners();

        ConsoleUtils.setupConsole();

        injectGuice();

        MinestomACR.init();

        final String ip = getConfig().getString("ip");
        final int port = getConfig().getInt("port");
        minecraftServer.start(ip, port);
    }

    private void registerListeners() {
        final var handler = MinecraftServer.getGlobalEventHandler();
        handler.addListener(new APCListener());
        handler.addListener(new PJListener());
        handler.addListener(new BBListener());
    }

    private void injectGuice() {
        final Injector injector = Guice.createInjector(new SlimeModule(this));

        final BrandAnimator animator = injector.getInstance(BrandAnimator.class);
        animator.startAnimation();
    }

}