package cc.davyy.slime.module;

import cc.davyy.slime.SlimeLoader;
import cc.davyy.slime.commands.*;
import cc.davyy.slime.managers.*;
import cc.davyy.slime.model.HologramFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class SlimeModule extends AbstractModule {

    private final SlimeLoader instance;

    public SlimeModule(SlimeLoader instance) {
        this.instance = instance;
    }

    @Override
    protected void configure() {
        bind(SlimeLoader.class).toInstance(instance);

        // Manager Binding
        bind(BossBarManager.class);
        bind(BrandManager.class);
        bind(LobbyManager.class);
        bind(SidebarManager.class);
        bind(HologramManager.class);
        bind(BroadcastManager.class);
        bind(SpawnManager.class);
        bind(GameModeManager.class);

        // Command Binding
        bind(BroadCastCommand.class);
        bind(ConfigReloadCommand.class);
        bind(DebugCommand.class);
        bind(GameModeCommand.class);
        bind(HologramCommand.class);
        bind(ListCommandsCommand.class);
        bind(LobbyCommand.class);
        bind(NPCCommand.class);
        bind(SpawnCommand.class);
        bind(StopCommand.class);
    }

    @Provides
    @Singleton
    public HologramFactory provideHologramFactory() {
        return new HologramFactory();
    }

}