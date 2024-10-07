package cc.davyy.slime.guice.modules;

import cc.davyy.slime.factories.DisguiseFactory;
import cc.davyy.slime.factories.HologramFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class FactoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DisguiseFactory.class).in(Singleton.class);
        bind(HologramFactory.class).in(Singleton.class);
    }

}