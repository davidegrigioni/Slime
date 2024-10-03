package cc.davyy.slime.guice;

import cc.davyy.slime.listeners.*;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class ListenerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AsyncConfigListener.class).in(Singleton.class);
        bind(InventoryPreClickListener.class).in(Singleton.class);
        bind(PlayerBlockBreakListener.class).in(Singleton.class);
        bind(PlayerChatListener.class).in(Singleton.class);
        bind(PlayerDisconnectListener.class).in(Singleton.class);
        bind(PlayerEntityInteractListener.class).in(Singleton.class);
        bind(PlayerItemUseListener.class).in(Singleton.class);
        bind(PlayerJoinListener.class).in(Singleton.class);
        bind(PlayerMoveListener.class).in(Singleton.class);
        bind(PlayerPacketListener.class).in(Singleton.class);
    }

}