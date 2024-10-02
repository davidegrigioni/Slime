package cc.davyy.slime.commands.player;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.gameplay.TeleportService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.cooldown.Cooldown;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import net.minestom.server.coordinate.Pos;

import java.time.temporal.ChronoUnit;

@Command(name = "teleport", aliases = "tp")
@Permission("slime.teleport")
@Cooldown(key = "teleport-cooldown", count = 10, unit = ChronoUnit.SECONDS, bypass = "slime.admin")
@Singleton
public class TeleportCommand {

    private final TeleportService teleportService;

    @Inject
    public TeleportCommand(TeleportService teleportService) {
        this.teleportService = teleportService;
    }

    @Execute
    void onPosTeleport(@Context SlimePlayer player, @Arg Pos position) {
        teleportService.teleportPlayerToCoordinates(player, position);
    }

    @Execute
    void onTeleport(@Context SlimePlayer player, @Arg SlimePlayer target) {
        teleportService.teleportPlayerToTarget(player, target);
    }

    @Execute
    void onTargetToPlayerTeleport(@Context SlimePlayer executor, @Arg SlimePlayer target) {
        teleportService.teleportTargetToExecutor(executor, target);
    }

}