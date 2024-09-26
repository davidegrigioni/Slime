package cc.davyy.slime.commands.admin;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.CommandResult;
import net.minestom.server.command.builder.arguments.ArgumentCommand;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.GeneralUtils.hasPlayerPermission;

@Singleton
public class ExecuteCommand extends Command {

    private final ArgumentEntity playerArg = ArgumentType.Entity("player").onlyPlayers(true);
    private final ArgumentCommand executeCommandArg = ArgumentType.Command("command");

    @Inject
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
        final SlimePlayer target = (SlimePlayer) context.get(playerArg).findFirstPlayer(player);
        final String commandInput = command.getInput();

        MinecraftServer.getCommandManager().execute(target, commandInput);

        player.sendMessage(Messages.EXECUTE_COMMAND_MESSAGE
                .addPlaceholder("command", commandInput)
                .addPlaceholder("target", player.getUsername())
                .asComponent());
    }

}