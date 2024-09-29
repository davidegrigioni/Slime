package cc.davyy.slime.module;

import cc.davyy.slime.managers.general.ConfigManager;
import com.google.inject.Provider;

public class ConfigManagerProvider implements Provider<ConfigManager> {

    @Override
    public ConfigManager get() {
        return new ConfigManager();
    }

}