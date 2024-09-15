package cc.davyy.slime.commands;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static cc.davyy.slime.utils.GeneralUtils.hasPlayerPermission;

@Singleton
public class ListCommandsCommand extends Command {

    private final Set<Command> commands = MinecraftServer.getCommandManager().getCommands();

    public ListCommandsCommand() {
        super("listcommands");

        setCondition((sender, commandString) -> {
            if (!hasPlayerPermission(sender, "slime.listcommands")) {
                sender.sendMessage(Messages.NO_PERMS.asComponent());
                return false;
            }
            return true;
        });

        setDefaultExecutor(this::execute);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        commands.forEach(command -> player.sendMessage(command.getNames()));
    }

}