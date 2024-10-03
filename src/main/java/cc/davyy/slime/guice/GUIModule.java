package cc.davyy.slime.guice;

import cc.davyy.slime.gui.LobbyGUI;
import cc.davyy.slime.gui.ServerGUI;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class GUIModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(LobbyGUI.class).in(Singleton.class);
        bind(ServerGUI.class).in(Singleton.class);
    }

}