package cc.davyy.slime.commands;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import cc.davyy.slime.utils.PosUtils;
import com.asintoto.minestomacr.annotations.AutoRegister;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.FileUtils.getConfig;

@AutoRegister
public class SpawnCommand extends Command {

    public SpawnCommand() {
        super("spawn");

        setCondition(((sender, commandString) -> switch (sender) {
            case SlimePlayer player -> player.hasPermission("slime.spawn");
            case ConsoleSender ignored -> true;
            default -> false;
        }));

        setDefaultExecutor(this::spawn);

        addSubcommand(new SetSpawnCommand());
    }

    private void spawn(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        final String posString = getConfig().getString("spawn.position");
        final Pos pos = PosUtils.fromString(posString);

        Check.notNull(pos, "Position cannot be null, Check your Config!");

        player.teleport(pos).thenRun(() -> player.sendMessage(Messages.SPAWN_TELEPORT
                .asComponent()));
    }

    private static class SetSpawnCommand extends Command {

        public SetSpawnCommand() {
            super("setspawn");

            setCondition(((sender, commandString) -> switch (sender) {
                case SlimePlayer player -> player.hasPermission("slime.setspawn");
                case ConsoleSender ignored -> true;
                default -> false;
            }));

            setDefaultExecutor(this::setSpawn);
        }

        private void setSpawn(@NotNull CommandSender sender, @NotNull CommandContext context) {
            final Player player = (Player) sender;
            final Pos pos = player.getPosition();

            getConfig().set("spawn.position", PosUtils.toString(pos));
            player.sendMessage(Messages.SPAWN_SET
                    .addPlaceholder("pos", PosUtils.toString(pos))
                    .asComponent());
        }

    }

}