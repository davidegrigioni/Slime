package cc.davyy.slime.commands;

import cc.davyy.slime.managers.BroadcastManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.exception.ArgumentSyntaxException;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.GeneralUtils.getOnlineSlimePlayers;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.TextColor.color;

@Singleton
public class BroadCastCommand extends Command {

    private final BroadcastManager broadcastManager;

    private final ArgumentStringArray messageArgumentArray = ArgumentType.StringArray("message");
    private final ArgumentString titleArg = ArgumentType.String("titleArg");
    private final ArgumentString subTitleArg = ArgumentType.String("subtitleArg");

    private final ArgumentLiteral titleSubCommandArg = ArgumentType.Literal("title");

    @Inject
    public BroadCastCommand(BroadcastManager broadcastManager) {
        super("broadcast");
        this.broadcastManager = broadcastManager;

        //setCondition(((sender, commandString) -> hasPlayerPermission(sender, "slime.broadcast")));

        setDefaultExecutor(((commandSender, commandContext) -> {
            final String usage = """
                    /broadcast [message]\s
                    /broadcast title <title>\s
                    /broadcast title <title> <subtitle>""";

            final Component usageMessage = text("Usage Instructions:")
                    .color(color(255, 0, 0))
                    .append(newline())
                    .append(text(usage)
                            .color(color(255, 255, 255)));

            commandSender.sendMessage(usageMessage);
        }));

        setArgumentCallback(this::onSyntaxError, messageArgumentArray);

        addSyntax(this::executeBroadcast, messageArgumentArray);

        addSyntax(this::executeBroadcastTitle, titleSubCommandArg, titleArg);
        addSyntax(this::executeBroadcastTitleSub, titleSubCommandArg, titleArg, subTitleArg);
        addSyntax(this::executeBroadcastTitleSubTime, titleSubCommandArg, titleArg, subTitleArg);
    }

    private void onSyntaxError(@NotNull CommandSender sender, @NotNull ArgumentSyntaxException exception) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String input = exception.getInput();

        player.sendMessage("Syntax ERROR " + input + " cannot be empty");
    }

    private void executeBroadcast(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final String finalMessage = String.join(" ", context.get(messageArgumentArray));

        broadcastManager.broadcastMessage(sender, finalMessage);
    }

    private void executeBroadcastTitle(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String message = context.get(titleArg);

        broadcastManager.broadcastTitle(player, message, getOnlineSlimePlayers());
    }

    private void executeBroadcastTitleSub(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String message = context.get(titleArg);
        final String subTitle = context.get(subTitleArg);

        broadcastManager.broadcastTitle(player, message, subTitle, getOnlineSlimePlayers());
    }

    private void executeBroadcastTitleSubTime(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String message = context.get(titleArg);
        final String subTitle = context.get(subTitleArg);

        broadcastManager.broadcastTitleWithTimes(player, message, subTitle, getOnlineSlimePlayers());
    }

}