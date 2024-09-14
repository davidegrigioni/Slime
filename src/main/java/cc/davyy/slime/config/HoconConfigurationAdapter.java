package cc.davyy.slime.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import me.lucko.luckperms.common.config.generic.adapter.ConfigurateConfigAdapter;
import me.lucko.luckperms.common.config.generic.adapter.ConfigurationAdapter;
import me.lucko.luckperms.common.plugin.LuckPermsPlugin;
import me.lucko.luckperms.minestom.LPMinestomPlugin;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public final class HoconConfigurationAdapter extends ConfigurateConfigAdapter implements ConfigurationAdapter {

    private static final String LUCKPERMS_FOLDER = "luckperms";
    private static final String CONFIG_FILE_NAME = "luckperms.conf";

    public HoconConfigurationAdapter(LuckPermsPlugin plugin) {
        super(plugin, ((LPMinestomPlugin) plugin).resolveConfig(LUCKPERMS_FOLDER + "/" + CONFIG_FILE_NAME));
        copyDefaultConfig(((LPMinestomPlugin) plugin).resolveConfig(LUCKPERMS_FOLDER + "/" + CONFIG_FILE_NAME));
    }

    @Override
    protected ConfigurationLoader<? extends ConfigurationNode> createLoader(Path path) {
        return HoconConfigurationLoader.builder().setPath(path).build();
    }

    /**
     * Copies the default configuration file from the resources to the file system
     * if it does not already exist.
     *
     * @param configPath the path where the configuration should be located.
     */
    private void copyDefaultConfig(Path configPath) {
        if (!Files.exists(configPath)) {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
                if (inputStream == null) {
                    throw new IllegalStateException("Default configuration file '" + CONFIG_FILE_NAME + "' not found in resources.");
                }
                Files.createDirectories(configPath.getParent()); // Ensure the directory exists
                Files.copy(inputStream, configPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Default " + CONFIG_FILE_NAME + " copied to " + configPath);
            } catch (IOException ex) {
                throw new RuntimeException("Failed to copy default " + CONFIG_FILE_NAME + " to " + configPath, ex);
            }
        }
    }

}