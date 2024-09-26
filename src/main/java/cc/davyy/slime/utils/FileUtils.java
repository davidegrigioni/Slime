package cc.davyy.slime.utils;

import cc.davyy.slime.config.HoconConfigurationAdapter;
import cc.davyy.slime.model.SlimePlayer;
import me.lucko.luckperms.common.config.generic.adapter.EnvironmentVariableConfigAdapter;
import me.lucko.luckperms.common.config.generic.adapter.MultiConfigurationAdapter;
import me.lucko.luckperms.minestom.CommandRegistry;
import me.lucko.luckperms.minestom.LuckPermsMinestom;
import net.luckperms.api.LuckPerms;
import net.minestom.server.MinecraftServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for managing configuration files and integrating LuckPerms.
 * <p>
 * This class handles loading, reloading, and accessing configuration and messages files,
 * as well as setting up the LuckPerms integration for permissions management.
 */
public final class FileUtils {

    private FileUtils() {}

    public static void setupFiles() {
        setupLuckPerms();
        //setupPayments();
    }

    /**
     * Sets up LuckPerms integration with the plugin.
     * <p>
     * Initializes LuckPerms with a configuration directory and a set of adapters
     * for environment variables and HOCON configuration.
     * <p>
     * The setup enables command registry and dependency management for LuckPerms.
     */
    private static void setupLuckPerms() {
        final Path luckPermsDir = Paths.get(System.getProperty("user.dir"), "luckperms");

        if (!Files.exists(luckPermsDir)) {
            try {
                Files.createDirectories(luckPermsDir);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create LuckPerms directory!", e);
            }
        }

        final LuckPerms luckPerms = LuckPermsMinestom.builder(luckPermsDir)
                .commandRegistry(CommandRegistry.minestom())
                .configurationAdapter(plugin -> new MultiConfigurationAdapter(plugin,
                        new EnvironmentVariableConfigAdapter(plugin),
                        new HoconConfigurationAdapter(plugin)))
                .dependencyManager(true)
                .enable();
        MinecraftServer.getConnectionManager().setPlayerProvider((uuid, username, connection) -> new SlimePlayer(luckPerms, uuid, username, connection));
    }

}