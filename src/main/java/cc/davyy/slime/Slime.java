package cc.davyy.slime;

import cc.davyy.slime.listeners.APCListener;
import cc.davyy.slime.listeners.PJListener;
import cc.davyy.slime.misc.CommandHandler;
import net.minestom.server.MinecraftServer;

import static cc.davyy.slime.utils.FileUtils.*;

public class Slime {

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();

        setupLuckPerms();
        setupConfig();

        registerListeners();

        CommandHandler commandHandler = new CommandHandler();
        commandHandler.setupConsole();

        final String ip = getConfig().getString("ip");
        final int port = getConfig().getInt("port");
        minecraftServer.start(ip, port);
    }

    private static void registerListeners() {
        final var handler = MinecraftServer.getGlobalEventHandler();
        handler.addListener(new APCListener());
        handler.addListener(new PJListener());
    }

}