package cc.davyy.slime.managers.general;

import cc.davyy.slime.commands.DisguiseCommand;
import cc.davyy.slime.commands.ItemDisplayCommand;
import cc.davyy.slime.commands.admin.*;
import cc.davyy.slime.commands.entities.HologramCommand;
import cc.davyy.slime.commands.entities.NPCCommand;
import cc.davyy.slime.commands.player.SidebarCommand;
import cc.davyy.slime.commands.player.SocialCommand;
import cc.davyy.slime.commands.player.SpawnCommand;
import cc.davyy.slime.commands.player.TeleportCommand;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;

@Singleton
public class CommandManager {

    private final BroadCastCommand broadcastCommand;
    private final ExecuteCommand executeCommand;
    private final GameModeCommand gameModeCommand;
    private final LobbyCommand lobbyCommand;
    private final SayCommand sayCommand;
    private final SetSpawnCommand setSpawnCommand;
    private final SlimeCommand slimeCommand;
    private final StopCommand stopCommand;
    private final HologramCommand hologramCommand;
    private final NPCCommand npcCommand;
    private final SidebarCommand sidebarCommand;
    private final SocialCommand socialCommand;
    private final SpawnCommand spawnCommand;
    private final TeleportCommand teleportCommand;
    private final ItemDisplayCommand itemDisplayCommand;
    private final DisguiseCommand disguiseCommand;

    @Inject
    public CommandManager(BroadCastCommand broadcastCommand,
                          ExecuteCommand executeCommand,
                          GameModeCommand gameModeCommand,
                          LobbyCommand lobbyCommand,
                          SayCommand sayCommand,
                          SetSpawnCommand setSpawnCommand,
                          SlimeCommand slimeCommand,
                          StopCommand stopCommand,
                          HologramCommand hologramCommand,
                          NPCCommand npcCommand,
                          SidebarCommand sidebarCommand,
                          SocialCommand socialCommand,
                          SpawnCommand spawnCommand,
                          TeleportCommand teleportCommand,
                          ItemDisplayCommand itemDisplayCommand,
                          DisguiseCommand disguiseCommand) {
        this.broadcastCommand = broadcastCommand;
        this.executeCommand = executeCommand;
        this.gameModeCommand = gameModeCommand;
        this.lobbyCommand = lobbyCommand;
        this.sayCommand = sayCommand;
        this.setSpawnCommand = setSpawnCommand;
        this.slimeCommand = slimeCommand;
        this.stopCommand = stopCommand;
        this.hologramCommand = hologramCommand;
        this.npcCommand = npcCommand;
        this.sidebarCommand = sidebarCommand;
        this.socialCommand = socialCommand;
        this.spawnCommand = spawnCommand;
        this.teleportCommand = teleportCommand;
        this.itemDisplayCommand = itemDisplayCommand;
        this.disguiseCommand = disguiseCommand;
    }

    public void init() {
        final var commandRegistry = MinecraftServer.getCommandManager();

        commandRegistry.register(broadcastCommand,
                executeCommand,
                gameModeCommand,
                lobbyCommand,
                sayCommand,
                setSpawnCommand,
                slimeCommand,
                stopCommand,
                hologramCommand,
                npcCommand,
                sidebarCommand,
                socialCommand,
                spawnCommand,
                teleportCommand,
                disguiseCommand,
                itemDisplayCommand);
    }

}