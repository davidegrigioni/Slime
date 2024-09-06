package cc.davyy.slime.module;

import cc.davyy.slime.SlimeLoader;
import cc.davyy.slime.managers.*;
import com.google.inject.AbstractModule;

public class SlimeModule extends AbstractModule {

    private final SlimeLoader instance;

    public SlimeModule(SlimeLoader instance) {
        this.instance = instance;
    }

    @Override
    protected void configure() {
        bind(SlimeLoader.class).toInstance(instance);

        bind(BrandManager.class);
        bind(ChatTranslatorManager.class);
        bind(LobbyManager.class);
        bind(RegionManager.class);
        bind(SidebarManager.class);
        bind(HologramManager.class);
        //bind(NPCManager.class).in(singleton);
    }

}