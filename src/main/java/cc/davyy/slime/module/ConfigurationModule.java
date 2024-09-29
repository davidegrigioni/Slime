package cc.davyy.slime.module;

import cc.davyy.slime.managers.general.ConfigManager;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.name.Named;

public class ConfigurationModule extends AbstractModule {

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
    public String provideGroupFormat(@Named("group") String group) {
        return configManagerProvider.get().getUi().getString("group-formats." + group);
    }

}