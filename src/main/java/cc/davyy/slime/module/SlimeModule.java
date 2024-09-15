package cc.davyy.slime.module;

import cc.davyy.slime.SlimeLoader;
import cc.davyy.slime.commands.*;
import cc.davyy.slime.cosmetics.CosmeticService;
import cc.davyy.slime.cosmetics.managers.ItemCosmeticManager;
import cc.davyy.slime.entities.holograms.HologramFactory;
import cc.davyy.slime.entities.npc.NPCFactory;
import cc.davyy.slime.gui.LobbyGUI;
import cc.davyy.slime.gui.ServerGUI;
import cc.davyy.slime.services.*;
import cc.davyy.slime.listeners.EventsListener;
import cc.davyy.slime.managers.*;
import cc.davyy.slime.cosmetics.CosmeticFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.ItemStack;

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
        bind(GameModeManager.class);
        bind(HologramManager.class);
        bind(LobbyManager.class);
        bind(NPCManager.class);
        bind(SidebarManager.class);
        bind(SpawnManager.class);
        bind(TeleportManager.class);
        bind(ItemCosmeticManager.class);

        // Interface Binding
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

        // Command Binding
        bind(BroadCastCommand.class);
        bind(ConfigReloadCommand.class);
        bind(CosmeticCommand.class);
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

        bind(CosmeticFactory.class);
        bind(HologramFactory.class);
        bind(NPCFactory.class);

        bind(new TypeLiteral<CosmeticService<ItemStack>>() {})
                .to(ItemCosmeticManager.class);
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