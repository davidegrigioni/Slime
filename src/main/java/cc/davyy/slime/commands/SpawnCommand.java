package cc.davyy.slime.commands;

import cc.davyy.slime.utils.PosUtils;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.FileUtils.getConfig;

public class SpawnCommand extends Command {

    public SpawnCommand() {
        super("spawn");

        setCondition(((sender, commandString) -> {
            final Player player = (Player) sender;
            return player.hasPermission("slime.spawn");
        }));

        setDefaultExecutor(this::spawn);

        addSubcommand(new SetSpawnCommand());
    }

    private void spawn(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        final String posString = getConfig().getString("spawn-pos");
        final Pos pos = PosUtils.fromString(posString);

        if (pos != null) {
            player.teleport(pos);
            player.sendMessage("You got teleported to " + PosUtils.toString(pos));
        }

    }

    private static class SetSpawnCommand extends Command {

        public SetSpawnCommand() {
            super("setspawn");

            setCondition(((sender, commandString) -> {
                final Player player = (Player) sender;
                return player.hasPermission("slime.setspawn");
            }));

            setDefaultExecutor(this::setSpawn);

        }

        private void setSpawn(@NotNull CommandSender sender, @NotNull CommandContext context) {
            final Player player = (Player) sender;
            final Pos pos = player.getPosition();

            getConfig().set("spawn-pos", PosUtils.toString(pos));
            player.sendMessage("Set spawn to " + PosUtils.toString(pos));
        }

    }

}