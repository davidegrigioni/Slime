package cc.davyy.slime.guice;

import cc.davyy.slime.guice.modules.*;
import cc.davyy.slime.guice.provider.ConfigManagerProvider;
import cc.davyy.slime.config.ConfigManager;
import com.google.inject.AbstractModule;

public class SlimeModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ConfigManager.class).toProvider(ConfigManagerProvider.class);

        install(new CommandModule());
        install(new ConfigurationModule(new ConfigManagerProvider()));
        install(new FactoryModule());
        install(new GUIModule());
        install(new InterfaceModule());
        install(new ListenerModule());
        install(new ManagerModule());
    }

}