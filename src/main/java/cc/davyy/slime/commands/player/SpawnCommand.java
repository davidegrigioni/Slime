package cc.davyy.slime.commands.player;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.gameplay.SpawnService;
import cc.davyy.slime.utils.Messages;
import cc.davyy.slime.utils.PosUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.GeneralUtils.hasPlayerPermission;

@Singleton
public class SpawnCommand extends Command {

    private final SpawnService spawnService;

    @Inject
    public SpawnCommand(SpawnService spawnService) {
        super("spawn");
        this.spawnService = spawnService;

        setCondition((sender, commandString) -> {
            if (!hasPlayerPermission(sender, "slime.spawn")) {
                sender.sendMessage(Messages.NO_PERMS.asComponent());
                return false;
            }
            return true;
        });

        setDefaultExecutor(this::spawn);

        addSubcommand(new SetSpawnCommand(spawnService));
    }

    private void spawn(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;

        final Pos spawnPos = spawnService.getSpawnPosition();
        Check.notNull(spawnPos, "Position cannot be null, check your config!");

        spawnService.teleportToSpawn(player);
    }

    @Singleton
    private static class SetSpawnCommand extends Command {

        private final SpawnService spawnService;

        @Inject
        public SetSpawnCommand(SpawnService spawnService) {
            super("setspawn");
            this.spawnService = spawnService;

            setCondition((sender, commandString) -> {
                if (!hasPlayerPermission(sender, "slime.setspawn")) {
                    sender.sendMessage(Messages.NO_PERMS.asComponent());
                    return false;
                }
                return true;
            });

            setDefaultExecutor(this::setSpawn);
        }

        private void setSpawn(@NotNull CommandSender sender, @NotNull CommandContext context) {
            final SlimePlayer player = (SlimePlayer) sender;
            final Pos pos = player.getPosition();

            spawnService.setSpawnPosition(pos);
            player.sendMessage(Messages.SPAWN_SET
                    .addPlaceholder("pos", PosUtils.toString(pos))
                    .asComponent());
        }

    }

}