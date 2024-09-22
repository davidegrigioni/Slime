package cc.davyy.slime.module;

import cc.davyy.slime.managers.*;
import cc.davyy.slime.managers.cosmetics.ArmorCosmeticManager;
import cc.davyy.slime.managers.cosmetics.ParticleCosmeticManager;
import cc.davyy.slime.managers.entities.HologramManager;
import cc.davyy.slime.services.*;
import cc.davyy.slime.services.cosmetics.ArmorCosmeticService;
import cc.davyy.slime.services.cosmetics.ParticleCosmeticService;
import cc.davyy.slime.services.entities.HologramService;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class InterfaceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ArmorCosmeticService.class).to(ArmorCosmeticManager.class).in(Singleton.class);
        bind(ParticleCosmeticService.class).to(ParticleCosmeticManager.class).in(Singleton.class);

        bind(HologramService.class).to(HologramManager.class).in(Singleton.class);

        bind(BossBarService.class).to(BossBarManager.class).in(Singleton.class);
        bind(BrandService.class).to(BrandManager.class).in(Singleton.class);
        bind(BroadcastService.class).to(BroadcastManager.class).in(Singleton.class);
        bind(GameModeService.class).to(GameModeManager.class).in(Singleton.class);
        bind(LobbyService.class).to(LobbyManager.class).in(Singleton.class);
        bind(SidebarService.class).to(SidebarManager.class).in(Singleton.class);
        bind(SpawnService.class).to(SpawnManager.class).in(Singleton.class);
        bind(TeleportService.class).to(TeleportManager.class).in(Singleton.class);
    }

}