package cc.davyy.slime.managers.general;

import cc.davyy.slime.commands.ItemDisplayCommand;
import cc.davyy.slime.commands.VehicleCommand;
import cc.davyy.slime.commands.admin.*;
import cc.davyy.slime.commands.cosmetic.CosmeticCommand;
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

    @Inject private BroadCastCommand broadcastCommand;
    @Inject private ExecuteCommand executeCommand;
    @Inject private GameModeCommand gameModeCommand;
    @Inject private LobbyCommand lobbyCommand;
    @Inject private SayCommand sayCommand;
    @Inject private SetSpawnCommand setSpawnCommand;
    @Inject private SlimeCommand slimeCommand;
    @Inject private StopCommand stopCommand; // fino a qui worka
    //@Inject private CosmeticCommand cosmeticCommand;
    @Inject private HologramCommand hologramCommand;
    @Inject private NPCCommand npcCommand;
    @Inject private SidebarCommand sidebarCommand;
    @Inject private SocialCommand socialCommand;
    @Inject private SpawnCommand spawnCommand;
    @Inject private TeleportCommand teleportCommand;
    @Inject private ItemDisplayCommand itemDisplayCommand;
    @Inject private VehicleCommand vehicleCommand;

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
                //cosmeticCommand,
                hologramCommand,
                npcCommand,
                sidebarCommand,
                socialCommand,
                spawnCommand,
                teleportCommand,
                itemDisplayCommand,
                vehicleCommand);
    }

}