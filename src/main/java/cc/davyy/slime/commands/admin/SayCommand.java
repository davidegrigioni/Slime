package cc.davyy.slime.commands.admin;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Singleton;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.GeneralUtils.hasPlayerPermission;

@Singleton
public class SayCommand extends Command {

    private final ArgumentStringArray messageArray = ArgumentType.StringArray("message");
    private final ArgumentEntity playerArg = ArgumentType.Entity("player").onlyPlayers(true);

    public SayCommand() {
        super("say");

        setCondition((sender, commandString) -> {
            if (!hasPlayerPermission(sender, "slime.say")) {
                sender.sendMessage(Messages.NO_PERMS.asComponent());
                return false;
            }
            return true;
        });

        addSyntax(this::execute, playerArg, messageArray);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final SlimePlayer target = (SlimePlayer) context.get(playerArg).findFirstPlayer(player);
        final String finalMessage = String.join(" ", context.get(messageArray));

        target.sendMessage(target.getChatFormat(finalMessage));
    }

}