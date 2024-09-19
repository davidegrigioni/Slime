package cc.davyy.slime.module;

import cc.davyy.slime.commands.admin.LobbyCommand;
import cc.davyy.slime.gui.LobbyGUI;
import cc.davyy.slime.gui.ServerGUI;
import cc.davyy.slime.managers.LobbyManager;
import cc.davyy.slime.services.LobbyService;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class LobbyModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(LobbyService.class).to(LobbyManager.class);

        bind(LobbyManager.class);

        bind(LobbyCommand.class);
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