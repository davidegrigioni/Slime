package cc.davyy.slime.guice.modules;

import cc.davyy.slime.database.DisguiseDatabase;
import cc.davyy.slime.database.HologramDatabase;
import cc.davyy.slime.config.ConfigManager;
import com.google.inject.*;
import com.google.inject.name.Named;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class ConfigurationModule extends AbstractModule {

    private static final String DB_FOLDER = "configs/databases/";
    private static final File dbFolder = new File(DB_FOLDER);

    private static Path dbPath;

    private final Provider<ConfigManager> configManagerProvider;

    @Inject
    public ConfigurationModule(Provider<ConfigManager> configManagerProvider) {
        this.configManagerProvider = configManagerProvider;
    }

    @Provides
    @Named("scoreboardTitle")
    public String provideSidebarTitle() {
        return configManagerProvider.get().getUi().getString("scoreboard.title");
    }

    @Provides
    @Named("lobbyGuiTitle")
    public String provideLobbyGuiTitle() {
        return configManagerProvider.get().getUi().getString("lobby-gui-title");
    }

    @Provides
    @Named("serverGUITitle")
    public String provideServerGUITitle() {
        return configManagerProvider.get().getUi().getString("server-gui-title");
    }

    @Provides
    @Named("defaultChatFormat")
    public String provideDefaultChatFormat() {
        return configManagerProvider.get().getUi().getString("chat-format");
    }

    @Provides
    @Named("groupFormat")
    public String provideGroupFormat(@NotNull String group) {
        return configManagerProvider.get().getUi().getString("group-formats." + group);
    }

    @Provides
    static HologramDatabase provideHologramDatabase() throws SQLException {
        try {
            ensureDbFolderExists();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        dbPath = Paths.get(dbFolder.getAbsolutePath(), "hologram.db");

        return new HologramDatabase(dbPath.toString());
    }

    @Provides
    static DisguiseDatabase provideDisguiseDatabase() throws SQLException {
        try {
            ensureDbFolderExists();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        dbPath = Paths.get(dbFolder.getAbsolutePath(), "disguise.db");

        return new DisguiseDatabase(dbPath.toString());
    }

    private static void ensureDbFolderExists() throws IOException {
        if (!dbFolder.exists() && !dbFolder.mkdirs()) {
            throw new IOException("Failed to create directory: " + dbFolder.getAbsolutePath());
        }
    }

}