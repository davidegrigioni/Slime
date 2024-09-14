package cc.davyy.slime.module;

import cc.davyy.slime.SlimeLoader;
import cc.davyy.slime.commands.*;
import cc.davyy.slime.gui.LobbyGUI;
import cc.davyy.slime.gui.ServerGUI;
import cc.davyy.slime.interfaces.*;
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
        bind(CosmeticManager.class);
        bind(GameModeManager.class);
        bind(HologramManager.class);
        bind(LobbyManager.class);
        bind(NPCManager.class);
        bind(SidebarManager.class);
        bind(SpawnManager.class);
        bind(TeleportManager.class);

        // Interface Binding
        bind(IBossBar.class).to(BossBarManager.class);
        bind(IBrand.class).to(BrandManager.class);
        bind(IBroadcast.class).to(BroadcastManager.class);
        bind(ICosmetics.class).to(CosmeticManager.class);
        bind(IGameMode.class).to(GameModeManager.class);
        bind(IHologram.class).to(HologramManager.class);
        bind(ILobby.class).to(LobbyManager.class);
        bind(INPC.class).to(NPCManager.class);
        bind(ISidebar.class).to(SidebarManager.class);
        bind(ISpawn.class).to(SpawnManager.class);
        bind(ITeleport.class).to(TeleportManager.class);

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