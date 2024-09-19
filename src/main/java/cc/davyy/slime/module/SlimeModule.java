package cc.davyy.slime.module;

import com.google.inject.AbstractModule;

public class SlimeModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new CosmeticModule());
        install(new LobbyModule());
        install(new EntitiesModule());
        install(new MiscModule());
    }

}