package cc.davyy.slime.guice.provider;

import cc.davyy.slime.managers.general.ConfigManager;
import com.google.inject.Provider;

public class ConfigManagerProvider implements Provider<ConfigManager> {

    @Override
    public ConfigManager get() {
        return new ConfigManager();
    }

}