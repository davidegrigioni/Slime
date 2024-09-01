package cc.davyy.slime.commands;

import cc.davyy.slime.utils.Messages;
import com.asintoto.minestomacr.annotations.AutoRegister;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.ColorUtils.of;

@AutoRegister
public class BroadCastCommand extends Command {

    private final ArgumentEnum<BroadcastType> broadcastTypeArgumentEnum = ArgumentType.Enum("type", BroadcastType.class)
            .setFormat(ArgumentEnum.Format.UPPER_CASED);
    private final ArgumentString messageArgument = ArgumentType.String("message");

    public BroadCastCommand() {
        super("broadcast");

        setCondition(((sender, commandString) -> {
            if (sender instanceof Player player) {
                return player.hasPermission("slime.broadcast");
            }
            return true;
        }));

        addSyntax(this::execute, broadcastTypeArgumentEnum, messageArgument);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final BroadcastType type = context.get(broadcastTypeArgumentEnum);
        final String message = context.get(messageArgument);
        final var onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers();

        if (message.isEmpty()) {
            sender.sendMessage(Messages.MESSAGE_EMPTY
                    .asComponent());
            return;
        }

        switch (type) {
            case TITLE -> {
                final Title title = Title.title(of(message)
                        .build(), Component.empty());
                onlinePlayers.forEach(player -> player.showTitle(title));
            }
            case CHAT -> onlinePlayers.forEach(player -> player.sendMessage(of(message)
                    .build()));
        }
    }

    private enum BroadcastType { TITLE, CHAT }

}