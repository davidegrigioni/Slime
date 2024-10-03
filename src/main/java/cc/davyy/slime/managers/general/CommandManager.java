package cc.davyy.slime.managers.general;

import cc.davyy.slime.commands.DisguiseCommand;
import cc.davyy.slime.commands.VanishCommand;
import cc.davyy.slime.commands.admin.*;
import cc.davyy.slime.commands.entities.HologramCommand;
import cc.davyy.slime.commands.entities.NPCCommand;
import cc.davyy.slime.commands.player.SidebarCommand;
import cc.davyy.slime.commands.player.SocialCommand;
import cc.davyy.slime.commands.player.TeleportCommand;
import cc.davyy.slime.litecommands.EntityTypeArgument;
import cc.davyy.slime.litecommands.SlimePlayerProvider;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.minestom.LiteMinestomFactory;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.EntityType;

@Singleton
public class CommandManager {

    private final StopCommand stopCommand;
    private final SlimeCommand slimeCommand;
    private final SpawnCommand spawnCommand;
    private final SayCommand sayCommand;
    private final GameModeCommand gameModeCommand;
    private final ExecuteCommand executeCommand;
    private final BroadCastCommand broadCastCommand;
    private final LobbyCommand lobbyCommand;
    private final NPCCommand npcCommand;
    private final SidebarCommand sidebarCommand;
    private final SocialCommand socialCommand;
    private final TeleportCommand teleportCommand;
    private final DisguiseCommand disguiseCommand;
    private final HologramCommand hologramCommand;
    private final VanishCommand vanishCommand;

    private LiteCommands<CommandSender> liteCommands;

    @Inject
    public CommandManager(StopCommand stopCommand, SlimeCommand slimeCommand, SpawnCommand spawnCommand, SayCommand sayCommand, GameModeCommand gameModeCommand, ExecuteCommand executeCommand, BroadCastCommand broadCastCommand, LobbyCommand lobbyCommand, NPCCommand npcCommand, SidebarCommand sidebarCommand, SocialCommand socialCommand, TeleportCommand teleportCommand, DisguiseCommand disguiseCommand, HologramCommand hologramCommand, VanishCommand vanishCommand) {
        this.stopCommand = stopCommand;
        this.slimeCommand = slimeCommand;
        this.spawnCommand = spawnCommand;
        this.sayCommand = sayCommand;
        this.gameModeCommand = gameModeCommand;
        this.executeCommand = executeCommand;
        this.broadCastCommand = broadCastCommand;
        this.lobbyCommand = lobbyCommand;
        this.npcCommand = npcCommand;
        this.sidebarCommand = sidebarCommand;
        this.socialCommand = socialCommand;
        this.teleportCommand = teleportCommand;
        this.disguiseCommand = disguiseCommand;
        this.hologramCommand = hologramCommand;
        this.vanishCommand = vanishCommand;
    }

    public void init() {
        this.liteCommands = LiteMinestomFactory.builder()
                .context(SlimePlayer.class, new SlimePlayerProvider())
                .argument(EntityType.class, new EntityTypeArgument())
                .commands(broadCastCommand,
                        disguiseCommand,
                        teleportCommand,
                        socialCommand,
                        sidebarCommand,
                        npcCommand,
                        stopCommand,
                        slimeCommand,
                        vanishCommand,
                        sayCommand,
                        hologramCommand,
                        spawnCommand,
                        gameModeCommand,
                        lobbyCommand,
                        executeCommand)
                .build();
    }

}