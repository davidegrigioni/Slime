package cc.davyy.slime.commands;

import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Singleton
public class ListCommandsCommand extends Command {

    private final Set<Command> commands = MinecraftServer.getCommandManager().getCommands();

    @Inject
    public ListCommandsCommand() {
        super("listcommands");

        //setCondition(((sender, commandString) -> hasPlayerPermission(sender, "slime.listcommands")));

        setDefaultExecutor(this::execute);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        commands.forEach(command -> player.sendMessage(command.getNames()));
    }

}