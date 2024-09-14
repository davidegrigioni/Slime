package cc.davyy.slime.module;

import cc.davyy.slime.SlimeLoader;
import cc.davyy.slime.commands.*;
import cc.davyy.slime.gui.LobbyGUI;
import cc.davyy.slime.gui.ServerGUI;
import cc.davyy.slime.listeners.EventsListener;
import cc.davyy.slime.managers.*;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

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
        bind(BroadcastManager.class);
        bind(GameModeManager.class);
        bind(HologramManager.class);
        bind(LobbyManager.class);
        bind(SidebarManager.class);
        bind(SpawnManager.class);
        bind(TeleportManager.class);

        // Command Binding
        bind(BroadCastCommand.class);
        bind(ConfigReloadCommand.class);
        bind(GameModeCommand.class);
        bind(HologramCommand.class);
        bind(ListCommandsCommand.class);
        bind(LobbyCommand.class);
        bind(NPCCommand.class);
        bind(SocialCommand.class);
        bind(SpawnCommand.class);
        bind(StopCommand.class);
        bind(TeleportCommand.class);

        bind(EventsListener.class);
    }

    @Provides
    public LobbyGUI provideLobbyGUI(LobbyManager lobbyManager) {
        return new LobbyGUI(lobbyManager);
    }

    @Provides
    public ServerGUI provideServerGUI() {
        return new ServerGUI();
    }

}