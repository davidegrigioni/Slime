package cc.davyy.slime.module;

import cc.davyy.slime.gui.cosmetic.CosmeticGUI;
import cc.davyy.slime.gui.LobbyGUI;
import cc.davyy.slime.gui.ServerGUI;
import cc.davyy.slime.gui.cosmetic.HatGUI;
import cc.davyy.slime.gui.cosmetic.ParticleEffectGUI;
import cc.davyy.slime.gui.cosmetic.PetGUI;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class GUIModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(LobbyGUI.class).in(Singleton.class);
        bind(ServerGUI.class).in(Singleton.class);
        bind(CosmeticGUI.class).in(Singleton.class);
        bind(ParticleEffectGUI.class).in(Singleton.class);
        bind(HatGUI.class).in(Singleton.class);
        bind(PetGUI.class).in(Singleton.class);
    }

}