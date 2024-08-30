package cc.davyy.slime.module;

import cc.davyy.slime.SlimeLoader;
import cc.davyy.slime.misc.BrandAnimator;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class SlimeModule extends AbstractModule {

    private final SlimeLoader instance;

    public SlimeModule(SlimeLoader instance) {
        this.instance = instance;
    }

    @Override
    protected void configure() {
        bind(SlimeLoader.class).toInstance(instance);

        bind(BrandAnimator.class).in(Singleton.class);
    }

}