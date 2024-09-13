package cc.davyy.slime.module;

import cc.davyy.slime.SlimeLoader;
import cc.davyy.slime.commands.*;
import cc.davyy.slime.listeners.EventsListener;
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

        // Manager Binding
        bind(TeleportManager.class);
        bind(DisguiseManager.class);
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
        bind(ListCommandsCommand.class);

        bind(EventsListener.class);
    }

}