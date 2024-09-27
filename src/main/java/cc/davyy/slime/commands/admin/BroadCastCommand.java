package cc.davyy.slime.commands.admin;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.gameplay.BroadcastService;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.GeneralUtils.hasPlayerPermission;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;

@Singleton
public class BroadCastCommand extends Command {

    private final BroadcastService broadcastService;

    private final ArgumentStringArray messageArgumentArray = ArgumentType.StringArray("message");
    private final ArgumentString broadcastTitleArg = ArgumentType.String("titleArg");
    private final ArgumentString broadcastSubtitleArg = ArgumentType.String("subtitleArg");

    private final ArgumentLiteral titleSubCommandArg = ArgumentType.Literal("title");

    @Inject
    public BroadCastCommand(BroadcastService broadcastService) {
        super("broadcast");
        this.broadcastService = broadcastService;

        setCondition((sender, commandString) -> {
            if (!hasPlayerPermission(sender, "slime.broadcast")) {
                sender.sendMessage(Messages.NO_PERMS.asComponent());
                return false;
            }
            return true;
        });

        setDefaultExecutor(this::showUsage);

        addSyntax(this::executeBroadcast, messageArgumentArray);

        addSyntax(this::executeBroadcastTitle, titleSubCommandArg, broadcastTitleArg);
        addSyntax(this::executeBroadcastTitleSub, titleSubCommandArg, broadcastTitleArg, broadcastSubtitleArg);
        addSyntax(this::executeBroadcastTitleSubTime, titleSubCommandArg, broadcastTitleArg, broadcastSubtitleArg);
    }

    private void showUsage(@NotNull CommandSender commandSender, @NotNull CommandContext context) {
        final Component usageMessage = text("Usage Instructions:")
                .color(NamedTextColor.RED)
                .append(newline())
                .append(text("/broadcast [message]\n").color(NamedTextColor.WHITE))
                .append(text("/broadcast title <title>\n").color(NamedTextColor.WHITE))
                .append(text("/broadcast title <title> <subtitle>").color(NamedTextColor.WHITE));
        commandSender.sendMessage(usageMessage);
    }

    private void executeBroadcast(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final String finalMessage = String.join(" ", context.get(messageArgumentArray));

        broadcastService.broadcastMessage(sender, finalMessage);
    }

    private void executeBroadcastTitle(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String message = context.get(broadcastTitleArg);

        broadcastService.broadcastTitle(player, message);
    }

    private void executeBroadcastTitleSub(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String message = context.get(broadcastTitleArg);
        final String subTitle = context.get(broadcastSubtitleArg);

        broadcastService.broadcastTitle(player, message, subTitle);
    }

    private void executeBroadcastTitleSubTime(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String message = context.get(broadcastTitleArg);
        final String subTitle = context.get(broadcastSubtitleArg);

        broadcastService.broadcastTitleWithTimes(player, message, subTitle);
    }

}