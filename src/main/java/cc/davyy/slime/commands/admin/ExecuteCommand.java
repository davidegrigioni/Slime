package cc.davyy.slime.commands.admin;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.CommandResult;
import net.minestom.server.command.builder.arguments.ArgumentCommand;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.utils.entity.EntityFinder;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.GeneralUtils.hasPlayerPermission;

@Singleton
public class ExecuteCommand extends Command {

    private final ArgumentEntity playerArg = ArgumentType.Entity("player").onlyPlayers(true);
    private final ArgumentCommand executeCommandArg = ArgumentType.Command("command");

    public ExecuteCommand() {
        super("execute");

        setCondition((sender, commandString) -> {
            if (!hasPlayerPermission(sender, "slime.execute")) {
                sender.sendMessage(Messages.NO_PERMS.asComponent());
                return false;
            }
            return true;
        });

        addSyntax(this::execute, playerArg, executeCommandArg);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final CommandResult command = context.get(executeCommandArg);
        final EntityFinder finder = context.get(playerArg);
        final SlimePlayer target = (SlimePlayer) finder.findFirstPlayer(player);

        MinecraftServer.getCommandManager().execute(target, command.getInput());
        player.sendMessage("Player " + target.getUsername() + " executed command " + command.getInput());
    }

}