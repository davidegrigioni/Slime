package cc.davyy.slime.commands.entities;

import cc.davyy.slime.managers.entities.HologramManager;
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
    private final ArgumentLiteral addLineArg = ArgumentType.Literal("addline");
    private final ArgumentLiteral insertLineArg = ArgumentType.Literal("insertline");
    private final ArgumentLiteral removeLineArg = ArgumentType.Literal("removeline");
    private final ArgumentLiteral updateLineArg = ArgumentType.Literal("updateline");

    private final ArgumentString textArg = ArgumentType.String("text");
    private final ArgumentInteger idArg = ArgumentType.Integer("id");
    private final ArgumentInteger indexArg = ArgumentType.Integer("index");

    @Inject
    public HologramCommand(HologramManager hologramManager) {
        super("hologram", "holo");
        this.hologramManager = hologramManager;

        // Permission check
        setCondition((sender, commandString) -> {
            if (!hasPlayerPermission(sender, "slime.hologram")) {
                sender.sendMessage(Messages.NO_PERMS.asComponent());
                return false;
            }
            return true;
        });

        // Default executor: displays usage instructions
        setDefaultExecutor((commandSender, commandContext) -> {
            final Component usageMessage = text("Usage Instructions:")
                    .color(NamedTextColor.RED)
                    .append(newline())
                    .append(text("/hologram create <text>\n").color(NamedTextColor.WHITE))
                    .append(text("/hologram move <id>\n").color(NamedTextColor.WHITE))
                    .append(text("/hologram delete <id>\n").color(NamedTextColor.WHITE))
                    .append(text("/hologram addline <id> <text>\n").color(NamedTextColor.WHITE))
                    .append(text("/hologram insertline <id> <index> <text>\n").color(NamedTextColor.WHITE))
                    .append(text("/hologram removeline <id> <index>\n").color(NamedTextColor.WHITE))
                    .append(text("/hologram updateline <id> <index> <text>").color(NamedTextColor.WHITE));

            commandSender.sendMessage(usageMessage);
        });

        // Syntax for each command
        addSyntax(this::handleCreate, createArg, textArg);
        addSyntax(this::handleMove, moveArg, idArg);
        addSyntax(this::handleDelete, deleteArg, idArg);
        addSyntax(this::handleAddLine, addLineArg, idArg, textArg);
        addSyntax(this::handleInsertLine, insertLineArg, idArg, indexArg, textArg);
        addSyntax(this::handleRemoveLine, removeLineArg, idArg, indexArg);
        addSyntax(this::handleUpdateLine, updateLineArg, idArg, indexArg, textArg);
    }

    private void handleCreate(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String text = context.get(textArg);

        hologramManager.createHologram(player, of(text).parseLegacy().build());
    }

    private void handleMove(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final int id = context.get(idArg);

        hologramManager.moveHologram(id, player);
    }

    private void handleDelete(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final int id = context.get(idArg);

        hologramManager.deleteHologram(id, player);
    }

    private void handleAddLine(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final int id = context.get(idArg);
        final String text = context.get(textArg);

        hologramManager.addHologramLine(id, of(text).parseLegacy().build());
        /*player.sendMessage(Messages.HOLOGRAM_LINE_ADDED
                .addPlaceholder("id", String.valueOf(id))
                .asComponent());*/
    }

    private void handleInsertLine(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final int id = context.get(idArg);
        final int index = context.get(indexArg);
        final String text = context.get(textArg);

        hologramManager.insertHologramLine(id, index, of(text).parseLegacy().build());
        /*player.sendMessage(Messages.HOLOGRAM_LINE_INSERTED
                .addPlaceholder("id", String.valueOf(id))
                .addPlaceholder("index", String.valueOf(index))
                .asComponent());*/
    }

    private void handleRemoveLine(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final int id = context.get(idArg);
        final int index = context.get(indexArg);

        hologramManager.removeHologramLine(id, index);
        /*player.sendMessage(Messages.HOLOGRAM_LINE_REMOVED
                .addPlaceholder("id", String.valueOf(id))
                .addPlaceholder("index", String.valueOf(index))
                .asComponent());*/
    }

    private void handleUpdateLine(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final int id = context.get(idArg);
        final int index = context.get(indexArg);
        final String newText = context.get(textArg);

        hologramManager.updateHologramLine(id, index, of(newText).parseLegacy().build());
        /*player.sendMessage(Messages.HOLOGRAM_LINE_UPDATED
                .addPlaceholder("id", String.valueOf(id))
                .addPlaceholder("index", String.valueOf(index))
                .asComponent());*/
    }

}