package cc.davyy.slime.module;

import com.google.inject.AbstractModule;

public class SlimeModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new GUIModule());
        install(new FactoryModule());
        install(new InterfaceModule());
        install(new ManagerModule());
        install(new CommandModule());
    }

}