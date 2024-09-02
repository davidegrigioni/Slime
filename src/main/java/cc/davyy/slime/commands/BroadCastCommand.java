package cc.davyy.slime.commands;

import cc.davyy.slime.utils.Messages;
import com.asintoto.minestomacr.annotations.AutoRegister;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentLong;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Collection;

import static cc.davyy.slime.utils.ColorUtils.*;

@AutoRegister
public class BroadCastCommand extends Command {

    private final ArgumentStringArray messageArgumentArray = ArgumentType.StringArray("message");
    private final ArgumentString titleArg = ArgumentType.String("title");
    private final ArgumentString subTitleArg = ArgumentType.String("subtitle");
    private final Collection<Player> onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers();

    private final ArgumentLong fadeInArg = ArgumentType.Long("fadeIn");
    private final ArgumentLong stayArg = ArgumentType.Long("stay");
    private final ArgumentLong fadeOutArg = ArgumentType.Long("fadeOut");

    public BroadCastCommand() {
        super("broadcast");

        setCondition(((sender, commandString) -> {
            if (sender instanceof Player player) {
                return player.hasPermission("slime.broadcast");
            }
            return true;
        }));

        addSyntax(this::executeBroadcast, messageArgumentArray);
        addSyntax(this::executeBroadcastTitle, titleArg);
        addSyntax(this::executeBroadcastTitleSub, titleArg, subTitleArg);
        addSyntax(this::executeBroadcastTitleSubTime, titleArg, subTitleArg, fadeInArg, stayArg, fadeOutArg);
    }

    private void executeBroadcast(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final String finalMessage = String.join("  ", context.get(messageArgumentArray));

        if (finalMessage.isEmpty()) {
            sender.sendMessage(Messages.MESSAGE_EMPTY
                    .asComponent());
            return;
        }

        broadcast(finalMessage);
    }

    private void executeBroadcastTitle(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final String message = context.get(titleArg);
        sendTitle(message, Component.empty(), sender);
    }

    private void executeBroadcastTitleSub(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final String message = context.get(titleArg);
        final String subTitle = context.get(subTitleArg);
        sendTitle(message, txt(subTitle), sender);
    }

    private void executeBroadcastTitleSubTime(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final String message = context.get(titleArg);
        final String subTitle = context.get(subTitleArg);

        final Long fadeIn = context.get(fadeInArg);
        final Long stay = context.get(stayArg);
        final Long fadeOut = context.get(fadeOutArg);

        final Title.Times times = Title.Times.times(Duration.ofSeconds(fadeIn), Duration.ofSeconds(stay), Duration.ofSeconds(fadeOut));
        sendTitle(message, txt(subTitle), times, sender);
    }

    private void sendTitle(@NotNull String titleText, @NotNull Component subTitle, @NotNull CommandSender sender) {
        if (titleText.isEmpty()) {
            sender.sendMessage(Messages.MESSAGE_EMPTY
                    .asComponent());
            return;
        }

        final Title title = Title.title(of(titleText)
                .build(), subTitle);
        onlinePlayers.forEach(player -> player.showTitle(title));
    }

    private void sendTitle(@NotNull String titleText, @NotNull Component subTitle, @NotNull Title.Times times, @NotNull CommandSender sender) {
        if (titleText.isEmpty()) {
            sender.sendMessage(Messages.MESSAGE_EMPTY
                    .asComponent());
            return;
        }

        final Title title = Title.title(of(titleText)
                .build(), subTitle, times);
        onlinePlayers.forEach(player -> player.showTitle(title));
    }

}