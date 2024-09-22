package cc.davyy.slime.module;

import cc.davyy.slime.factories.CosmeticFactory;
import cc.davyy.slime.factories.HologramFactory;
import cc.davyy.slime.factories.VehicleFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class FactoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CosmeticFactory.class).in(Singleton.class);
        bind(HologramFactory.class).in(Singleton.class);
        bind(VehicleFactory.class).in(Singleton.class);
    }

}