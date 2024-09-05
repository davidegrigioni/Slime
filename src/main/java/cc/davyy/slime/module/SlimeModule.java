package cc.davyy.slime.module;

import cc.davyy.slime.SlimeLoader;
import cc.davyy.slime.managers.*;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class SlimeModule extends AbstractModule {

    private final SlimeLoader instance;
    private final Class<Singleton> singleton = Singleton.class;

    public SlimeModule(SlimeLoader instance) {
        this.instance = instance;
    }

    @Override
    protected void configure() {
        bind(SlimeLoader.class).toInstance(instance);

        bind(BrandManager.class).in(singleton);
        bind(ChatTranslatorManager.class).in(singleton);
        bind(LobbyManager.class).in(singleton);
        bind(MOTDManager.class).in(singleton);
        bind(RegionManager.class).in(singleton);
        bind(SidebarManager.class).in(singleton);
    }

}