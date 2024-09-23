package cc.davyy.slime.managers;

import cc.davyy.slime.commands.DebugCommand;
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
    @Inject private ConfigReloadCommand configReloadCommand;
    @Inject private CosmeticCommand cosmeticCommand;
    @Inject private ExecuteCommand executeCommand;
    @Inject private GameModeCommand gameModeCommand;
    @Inject private HologramCommand hologramCommand;
    @Inject private LobbyCommand lobbyCommand;
    @Inject private NPCCommand npcCommand;
    @Inject private SayCommand sayCommand;
    @Inject private SocialCommand socialCommand;
    @Inject private SpawnCommand spawnCommand;
    @Inject private StopCommand stopCommand;
    @Inject private TeleportCommand teleportCommand;
    @Inject private SidebarCommand sidebarCommand;
    @Inject private VehicleCommand vehicleCommand;
    @Inject private ItemDisplayCommand itemDisplayCommand;
    @Inject private DebugCommand debugCommand;

    public void init() {
        final var commandRegistry = MinecraftServer.getCommandManager();

        commandRegistry.register(broadcastCommand,
                configReloadCommand,
                cosmeticCommand,
                executeCommand,
                gameModeCommand,
                hologramCommand,
                lobbyCommand,
                npcCommand,
                sayCommand,
                socialCommand,
                spawnCommand,
                stopCommand,
                sidebarCommand,
                vehicleCommand,
                debugCommand,
                itemDisplayCommand,
                teleportCommand);
    }

}