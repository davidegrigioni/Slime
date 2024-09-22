package cc.davyy.slime.module;

import cc.davyy.slime.commands.ItemDisplayCommand;
import cc.davyy.slime.commands.VehicleCommand;
import cc.davyy.slime.commands.admin.*;
import cc.davyy.slime.commands.cosmetic.CosmeticCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.ArmorSubCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.HatSubCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.ParticleSubCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.PetSubCommand;
import cc.davyy.slime.commands.entities.HologramCommand;
import cc.davyy.slime.commands.entities.NPCCommand;
import cc.davyy.slime.commands.player.SidebarCommand;
import cc.davyy.slime.commands.player.SocialCommand;
import cc.davyy.slime.commands.player.SpawnCommand;
import cc.davyy.slime.commands.player.TeleportCommand;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class CommandModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BroadCastCommand.class).in(Singleton.class);
        bind(ConfigReloadCommand.class).in(Singleton.class);
        bind(ExecuteCommand.class).in(Singleton.class);
        bind(GameModeCommand.class).in(Singleton.class);
        bind(LobbyCommand.class).in(Singleton.class);
        bind(SayCommand.class).in(Singleton.class);
        bind(StopCommand.class).in(Singleton.class);

        bind(ArmorSubCommand.class).in(Singleton.class);
        bind(HatSubCommand.class).in(Singleton.class);
        bind(ParticleSubCommand.class).in(Singleton.class);
        bind(PetSubCommand.class).in(Singleton.class);
        bind(CosmeticCommand.class).in(Singleton.class);

        bind(HologramCommand.class).in(Singleton.class);
        bind(NPCCommand.class).in(Singleton.class);

        bind(SidebarCommand.class).in(Singleton.class);
        bind(SocialCommand.class).in(Singleton.class);
        bind(SpawnCommand.class).in(Singleton.class);
        bind(TeleportCommand.class).in(Singleton.class);

        bind(ItemDisplayCommand.class).in(Singleton.class);
        bind(VehicleCommand.class).in(Singleton.class);
    }

}