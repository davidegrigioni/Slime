package cc.davyy.slime.commands;

import cc.davyy.slime.managers.SpawnManager;
import cc.davyy.slime.utils.Messages;
import cc.davyy.slime.utils.PosUtils;
import com.google.inject.Inject;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.GeneralUtils.hasPlayerPermission;

public class SpawnCommand extends Command {

    private final SpawnManager spawnManager;

    @Inject
    public SpawnCommand(SpawnManager spawnManager) {
        super("spawn");
        this.spawnManager = spawnManager;

        setCondition(((sender, commandString) -> hasPlayerPermission(sender, "slime.spawn")));

        setDefaultExecutor(this::spawn);

        addSubcommand(new SetSpawnCommand(spawnManager));
    }

    private void spawn(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;

        final Pos spawnPos = spawnManager.getSpawnPosition();
        Check.notNull(spawnPos, "Position cannot be null, check your config!");

        spawnManager.teleportToSpawn(player);
    }

    private static class SetSpawnCommand extends Command {

        private final SpawnManager spawnManager;

        @Inject
        public SetSpawnCommand(SpawnManager spawnManager) {
            super("setspawn");
            this.spawnManager = spawnManager;

            setCondition(((sender, commandString) -> hasPlayerPermission(sender, "slime.setspawn")));

            setDefaultExecutor(this::setSpawn);
        }

        private void setSpawn(@NotNull CommandSender sender, @NotNull CommandContext context) {
            final Player player = (Player) sender;
            final Pos pos = player.getPosition();

            spawnManager.setSpawnPosition(pos);
            player.sendMessage(Messages.SPAWN_SET
                    .addPlaceholder("pos", PosUtils.toString(pos))
                    .asComponent());
        }

    }

}