package cc.davyy.slime.module;

import cc.davyy.slime.commands.admin.*;
import cc.davyy.slime.commands.player.SocialCommand;
import cc.davyy.slime.commands.player.SpawnCommand;
import cc.davyy.slime.commands.player.TeleportCommand;
import cc.davyy.slime.listeners.EventsListener;
import cc.davyy.slime.managers.*;
import cc.davyy.slime.services.*;
import com.google.inject.AbstractModule;

public class MiscModule extends AbstractModule {

    @Override
    protected void configure() {
        // Bind services to their implementations
        bind(BossBarService.class).to(BossBarManager.class);
        bind(BroadcastService.class).to(BroadcastManager.class);
        bind(GameModeService.class).to(GameModeManager.class);
        bind(SpawnService.class).to(SpawnManager.class);
        bind(TeleportService.class).to(TeleportManager.class);

        // Bind manager implementations directly
        bind(BossBarManager.class);
        bind(SpawnManager.class);
        bind(TeleportManager.class);

        // Bind commands
        bind(BroadCastCommand.class);
        bind(ConfigReloadCommand.class);
        bind(ExecuteCommand.class);
        bind(SayCommand.class);
        bind(StopCommand.class);
        bind(SocialCommand.class);
        bind(SpawnCommand.class);
        bind(TeleportCommand.class);

        // Bind listeners
        bind(EventsListener.class);
    }

}