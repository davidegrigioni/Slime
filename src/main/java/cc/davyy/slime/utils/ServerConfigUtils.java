package cc.davyy.slime.utils;

import cc.davyy.slime.config.ConfigManager;
import cc.davyy.slime.model.ServerMode;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ServerConfigUtils {

    private static final ComponentLogger LOGGER = ComponentLogger.logger(ServerConfigUtils.class);

    private ServerConfigUtils() {}

    public static ServerMode getServerModeFromConfig(@NotNull ConfigManager configManager) {
        final String modeString = configManager.getConfig().getString("network.type").toUpperCase();
        try {
            return ServerMode.valueOf(modeString);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Invalid server mode in config ({}), defaulting to ONLINE mode.", modeString);
            return ServerMode.ONLINE;
        }
    }

    public static String loadVelocitySecret() {
        final Path path = Paths.get("./configs/forwarding.secret");
        try {
            return Files.readString(path).trim();
        } catch (IOException ex) {
            LOGGER.error("Failed to read velocity secret from \"{}\". Velocity is enabled in config, aborting start.", path);
            throw new RuntimeException("Could not load Velocity secret", ex);
        }
    }

}