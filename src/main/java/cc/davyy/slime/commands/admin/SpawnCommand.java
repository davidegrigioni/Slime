package cc.davyy.slime.commands.admin;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.gameplay.SpawnService;
import cc.davyy.slime.utils.Messages;
import cc.davyy.slime.utils.PosUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.cooldown.Cooldown;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import net.minestom.server.coordinate.Pos;

import java.time.temporal.ChronoUnit;

@RootCommand
@Singleton
public class SpawnCommand {

    private final SpawnService spawnService;

    @Inject
    public SpawnCommand(SpawnService spawnService) {
        this.spawnService = spawnService;
    }

    @Execute(name = "spawn")
    @Permission("slime.spawn")
    @Cooldown(key = "spawn-cooldown", count = 10, unit = ChronoUnit.SECONDS, bypass = "slime.admin")
    void spawn(@Context SlimePlayer player) {
        final Pos spawnPos = spawnService.getSpawnPosition();

        spawnService.teleportToSpawn(player);
    }

    @Execute(name = "setspawn")
    @Permission("slime.setspawn")
    void setSpawn(@Context SlimePlayer player) {
        final Pos pos = player.getPosition();

        spawnService.setSpawnPosition(pos);
        player.sendMessage(Messages.SPAWN_SET
                .addPlaceholder("pos", PosUtils.toString(pos))
                .asComponent());
    }

}