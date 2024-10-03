package cc.davyy.slime.guice;

import cc.davyy.slime.database.DisguiseDatabase;
import cc.davyy.slime.database.HologramDatabase;
import cc.davyy.slime.config.ConfigManager;
import com.google.inject.*;
import com.google.inject.name.Named;

import java.sql.SQLException;

public class ConfigurationModule extends AbstractModule {

    private static final String DB_FOLDER = "configs/databases/";

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
    public String provideGroupFormat(String group) {
        return configManagerProvider.get().getUi().getString("group-formats." + group);
    }

    @Provides
    static HologramDatabase provideHologramDatabase() throws SQLException {
        return new HologramDatabase(DB_FOLDER + "hologram.db");
    }

    @Provides
    static DisguiseDatabase provideDisguiseDatabase() throws SQLException {
        return new DisguiseDatabase(DB_FOLDER + "disguise.db");
    }

}