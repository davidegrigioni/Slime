package cc.davyy.slime.utils;

import cc.davyy.slime.misc.HoconConfigurationAdapter;
import cc.davyy.slime.model.SlimePlayer;
import de.leonhard.storage.SimplixBuilder;
import de.leonhard.storage.Yaml;
import dev.expx.payments.PaymentHandler;
import dev.expx.payments.StoreType;
import me.lucko.luckperms.common.config.generic.adapter.EnvironmentVariableConfigAdapter;
import me.lucko.luckperms.common.config.generic.adapter.MultiConfigurationAdapter;
import me.lucko.luckperms.minestom.CommandRegistry;
import me.lucko.luckperms.minestom.LuckPermsMinestom;
import net.luckperms.api.LuckPerms;
import net.minestom.server.MinecraftServer;

import java.io.File;
import java.nio.file.Path;

/**
 * Utility class for managing configuration files and integrating LuckPerms.
 * <p>
 * This class handles loading, reloading, and accessing configuration and messages files,
 * as well as setting up the LuckPerms integration for permissions management.
 */
public final class FileUtils {

    /**
     * The folder where configuration files are stored.
     */
    private static final String CONFIG_FOLDER = "configs/";

    /**
     * The YAML configuration object for general settings.
     */
    private static Yaml config;

    /**
     * The YAML configuration object for messages.
     */
    private static Yaml messages;

    // Private constructor to prevent instantiation.
    private FileUtils() {}

    /**
     * Initializes the configuration files by loading them from the filesystem
     * and providing default resources.
     * <p>
     * The configuration files are loaded from the {@code CONFIG_FOLDER} directory:
     * - {@code config.yml} for general configuration
     * - {@code messages.yml} for message strings
     */
    public static void setupConfig() {
        config = SimplixBuilder.fromFile(new File(CONFIG_FOLDER, "config.yml"))
                .addInputStreamFromResource(CONFIG_FOLDER + "config.yml")
                .createYaml();
        messages = SimplixBuilder.fromFile(new File(CONFIG_FOLDER, "messages.yml"))
                .addInputStreamFromResource(CONFIG_FOLDER + "messages.yml")
                .createYaml();
    }

    /**
     * Sets up LuckPerms integration with the plugin.
     * <p>
     * Initializes LuckPerms with a configuration directory and a set of adapters
     * for environment variables and HOCON configuration.
     * <p>
     * The setup enables command registry and dependency management for LuckPerms.
     */
    public static void setupLuckPerms() {
        final Path dir = Path.of("luckperms");
        LuckPerms luckPerms = LuckPermsMinestom.builder(dir)
                .commandRegistry(CommandRegistry.minestom())
                .configurationAdapter(plugin -> new MultiConfigurationAdapter(plugin,
                        new EnvironmentVariableConfigAdapter(plugin),
                        new HoconConfigurationAdapter(plugin)))
                .dependencyManager(true)
                .enable();
        MinecraftServer.getConnectionManager().setPlayerProvider((uuid, username, connection) -> new SlimePlayer(luckPerms, uuid, username, connection));
    }

    public static void setupFiles() {
        setupConfig();
        setupLuckPerms();
        //setupPayments();
    }

    private static void setupPayments() {
        PaymentHandler.init(StoreType.CRAFTINGSTORE_STORE, Path.of("store"));
    }

    /**
     * Reloads the configuration and messages files.
     * <p>
     * This method forces a reload of both the general configuration and the
     * messages configuration, applying any changes made to the files.
     */
    public static void reloadConfig() {
        config.forceReload();
        messages.forceReload();
    }

    /**
     * Gets the current YAML configuration object for general settings.
     *
     * @return The YAML configuration object for general settings.
     */
    public static Yaml getConfig() { return config; }

    /**
     * Gets the current YAML configuration object for messages.
     *
     * @return The YAML configuration object for messages.
     */
    public static Yaml getMessages() { return messages; }

}