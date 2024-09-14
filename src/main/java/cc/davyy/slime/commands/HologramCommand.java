package cc.davyy.slime.commands;

import cc.davyy.slime.managers.HologramManager;
import cc.davyy.slime.model.SlimePlayer;
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
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.GeneralUtils.hasPlayerPermission;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;

@Singleton
public class HologramCommand extends Command {

    private final HologramManager hologramManager;

    private final ArgumentLiteral createArg = ArgumentType.Literal("create");
    private final ArgumentLiteral moveArg = ArgumentType.Literal("move");
    private final ArgumentLiteral deleteArg = ArgumentType.Literal("delete");

    private final ArgumentString textArg = ArgumentType.String("text");
    private final ArgumentInteger idArg = ArgumentType.Integer("id");

    @Inject
    public HologramCommand(HologramManager hologramManager) {
        super("hologram", "holo");
        this.hologramManager = hologramManager;

        setCondition((sender, commandString) -> {
            if (!hasPlayerPermission(sender, "slime.hologram")) {
                sender.sendMessage(Messages.NO_PERMS.asComponent());
                return false;
            }
            return true;
        });

        setDefaultExecutor((commandSender, commandContext) -> {
            final Component usageMessage = text("Usage Instructions:")
                    .color(NamedTextColor.RED)
                    .append(newline())
                    .append(text("/hologram create <text>")
                            .color(NamedTextColor.WHITE))
                    .append(text("/hologram move <id>")
                            .color(NamedTextColor.WHITE))
                    .append(text("/hologram delete <id>")
                            .color(NamedTextColor.WHITE));

            commandSender.sendMessage(usageMessage);
        });

        addSyntax(this::handleCreate, createArg, textArg);
        addSyntax(this::handleMove, moveArg, idArg);
        addSyntax(this::handleDelete, deleteArg, idArg);
    }

    private void handleDelete(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final int id = context.get(idArg);

        hologramManager.deleteHologram(id);
        player.sendMessage(text("Hologram " + id + " deleted.")
                .color(NamedTextColor.GREEN));
    }

    private void handleMove(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final int id = context.get(idArg);
        final Pos newPosition = player.getPosition();

        hologramManager.moveHologram(id, newPosition);
        player.sendMessage(text("Hologram " + id + " moved to position: " + newPosition)
                .color(NamedTextColor.GREEN));
    }

    private void handleCreate(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String text = context.get(textArg);

        hologramManager.createHologram(player, of(text)
                .parseLegacy()
                .build());
    }

}