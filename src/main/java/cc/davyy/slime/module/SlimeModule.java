package cc.davyy.slime.module;

import cc.davyy.slime.SlimeLoader;
import cc.davyy.slime.commands.admin.*;
import cc.davyy.slime.commands.cosmetic.CosmeticCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.ArmorSubCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.HatSubCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.ParticleSubCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.PetSubCommand;
import cc.davyy.slime.commands.player.SocialCommand;
import cc.davyy.slime.commands.player.SpawnCommand;
import cc.davyy.slime.commands.player.TeleportCommand;
import cc.davyy.slime.services.cosmetics.ArmorCosmeticService;
import cc.davyy.slime.services.cosmetics.ParticleCosmeticService;
import cc.davyy.slime.managers.cosmetics.ArmorCosmeticManager;
import cc.davyy.slime.managers.cosmetics.HatCosmeticManager;
import cc.davyy.slime.managers.cosmetics.ParticleCosmeticManager;
import cc.davyy.slime.managers.cosmetics.PetCosmeticManager;
import cc.davyy.slime.factories.HologramFactory;
import cc.davyy.slime.factories.NPCFactory;
import cc.davyy.slime.gui.LobbyGUI;
import cc.davyy.slime.gui.ServerGUI;
import cc.davyy.slime.services.*;
import cc.davyy.slime.listeners.EventsListener;
import cc.davyy.slime.managers.*;
import cc.davyy.slime.factories.CosmeticFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class SlimeModule extends AbstractModule {

    private final SlimeLoader instance;

    public SlimeModule(SlimeLoader instance) {
        this.instance = instance;
    }

    @Override
    protected void configure() {
        // Bind instances
        bind(SlimeLoader.class).toInstance(instance);

        // Manager Bindings
        bind(BossBarManager.class);
        bind(BrandManager.class);
        bind(BroadcastManager.class);
        bind(GameModeManager.class);
        bind(HologramManager.class);
        bind(LobbyManager.class);
        bind(NPCManager.class);
        bind(SidebarManager.class);
        bind(SpawnManager.class);
        bind(TeleportManager.class);

        bind(ArmorCosmeticManager.class);
        bind(HatCosmeticManager.class);
        bind(ParticleCosmeticManager.class);
        bind(PetCosmeticManager.class);

        bind(ParkourManager.class);

        // Interface Bindings
        bind(BossBarService.class).to(BossBarManager.class);
        bind(BrandService.class).to(BrandManager.class);
        bind(BroadcastService.class).to(BroadcastManager.class);
        bind(GameModeService.class).to(GameModeManager.class);
        bind(HologramService.class).to(HologramManager.class);
        bind(LobbyService.class).to(LobbyManager.class);
        bind(NPCService.class).to(NPCManager.class);
        bind(SidebarService.class).to(SidebarManager.class);
        bind(SpawnService.class).to(SpawnManager.class);
        bind(TeleportService.class).to(TeleportManager.class);

        bind(ArmorCosmeticService.class).to(ArmorCosmeticManager.class);
        bind(ParticleCosmeticService.class).to(ParticleCosmeticManager.class);

        // Command Bindings
        bind(BroadCastCommand.class);
        bind(ConfigReloadCommand.class);

        bind(CosmeticCommand.class);
        bind(ArmorSubCommand.class);
        bind(HatSubCommand.class);
        bind(ParticleSubCommand.class);
        bind(PetSubCommand.class);

        bind(GameModeCommand.class);
        bind(HologramCommand.class);
        bind(ListCommandsCommand.class);
        bind(LobbyCommand.class);
        bind(NPCCommand.class);
        bind(SocialCommand.class);
        bind(SpawnCommand.class);
        bind(StopCommand.class);
        bind(TeleportCommand.class);

        // Listener Bindings
        bind(EventsListener.class);

        // Factory Bindings
        bind(CosmeticFactory.class);
        bind(HologramFactory.class);
        bind(NPCFactory.class);
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