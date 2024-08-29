package cc.davyy.slime;

import cc.davyy.slime.commands.HologramCommand;
import cc.davyy.slime.commands.SpawnCommand;
import cc.davyy.slime.listeners.APCListener;
import cc.davyy.slime.listeners.PJListener;
import cc.davyy.slime.utils.ConsoleUtils;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.MinecraftServer;

import static cc.davyy.slime.utils.FileUtils.*;
import static cc.davyy.slime.utils.ColorUtils.of;

public class Slime {

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();

        setupLuckPerms();
        setupConfig();

        registerListeners();

        ConsoleUtils.setupConsole();

        final String brandName = getConfig().getString("brand-name");
        final String legacyComponent = LegacyComponentSerializer
                .legacyAmpersand()
                .serialize(of(brandName)
                .build());
        MinecraftServer.setBrandName(legacyComponent);

        MinecraftServer.getCommandManager().register(new SpawnCommand());
        MinecraftServer.getCommandManager().register(new HologramCommand());

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