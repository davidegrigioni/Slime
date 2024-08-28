package cc.davyy.slime.utils;

import cc.davyy.slime.misc.HoconConfigurationAdapter;
import de.leonhard.storage.SimplixBuilder;
import de.leonhard.storage.Yaml;
import me.lucko.luckperms.common.config.generic.adapter.EnvironmentVariableConfigAdapter;
import me.lucko.luckperms.common.config.generic.adapter.MultiConfigurationAdapter;
import me.lucko.luckperms.minestom.CommandRegistry;
import me.lucko.luckperms.minestom.LuckPermsMinestom;

import java.io.File;
import java.nio.file.Path;

public final class FileUtils {

    private final static String CONFIG_FOLDER = "configs/";
    private static Yaml config;

    private FileUtils() {}

    public static void setupConfig() {
        config = SimplixBuilder.fromFile(new File(CONFIG_FOLDER, "config.yml"))
                .addInputStreamFromResource(CONFIG_FOLDER + "config.yml")
                .createYaml();
    }

    public static void setupLuckPerms() {
        Path dir = Path.of("luckperms");
        LuckPermsMinestom.builder(dir)
                .commandRegistry(CommandRegistry.minestom())
                .configurationAdapter(plugin -> new MultiConfigurationAdapter(plugin,
                        new EnvironmentVariableConfigAdapter(plugin),
                        new HoconConfigurationAdapter(plugin)))
                .dependencyManager(true)
                .enable();
    }

    public static Yaml getConfig() { return config; }

}