package cc.davyy.slime.guice;

import cc.davyy.slime.commands.entities.DisguiseCommand;
import cc.davyy.slime.commands.entities.ItemDisplayCommand;
import cc.davyy.slime.commands.VanishCommand;
import cc.davyy.slime.commands.admin.*;
import cc.davyy.slime.commands.entities.HologramCommand;
import cc.davyy.slime.commands.entities.NPCCommand;
import cc.davyy.slime.commands.entities.SidebarCommand;
import cc.davyy.slime.commands.player.SocialCommand;
import cc.davyy.slime.commands.player.TeleportCommand;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class CommandModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BroadCastCommand.class).in(Singleton.class);
        bind(ExecuteCommand.class).in(Singleton.class);
        bind(GameModeCommand.class).in(Singleton.class);
        bind(LobbyCommand.class).in(Singleton.class);
        bind(SayCommand.class).in(Singleton.class);
        bind(SlimeCommand.class).in(Singleton.class);
        bind(SpawnCommand.class).in(Singleton.class);
        bind(StopCommand.class).in(Singleton.class);

        bind(HologramCommand.class).in(Singleton.class);
        bind(NPCCommand.class).in(Singleton.class);

        bind(SidebarCommand.class).in(Singleton.class);
        bind(SocialCommand.class).in(Singleton.class);
        bind(TeleportCommand.class).in(Singleton.class);

        bind(DisguiseCommand.class).in(Singleton.class);
        bind(ItemDisplayCommand.class).in(Singleton.class);
        bind(VanishCommand.class).in(Singleton.class);
    }

}