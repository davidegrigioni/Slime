package cc.davyy.slime.commands;

import cc.davyy.slime.managers.NPCManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.entity.PlayerSkin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;

@Singleton
public class NPCCommand extends Command {

    private final NPCManager npcManager;

    private final ArgumentWord actionArgument = ArgumentType.Word("action")
            .from("create", "move", "delete");

    private final ArgumentString nameArg = ArgumentType.String("name");
    private final ArgumentInteger idArg = ArgumentType.Integer("id");

    private final ArgumentString skinArg = ArgumentType.String("skin");

    @Inject
    public NPCCommand(NPCManager npcManager) {
        super("npc");
        this.npcManager = npcManager;

        setDefaultExecutor(this::showUsage);

        addSyntax(this::handleCommand, actionArgument, nameArg, idArg);
    }

    private void showUsage(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Component usageMessage = text("Usage Instructions:")
                .color(NamedTextColor.RED)
                .append(newline())
                .append(text("/npc create [name]")
                        .color(NamedTextColor.WHITE)
                        .append(text(" - Creates a new NPC with an optional name.")))
                .append(newline())
                .append(text("/npc move <id>")
                        .color(NamedTextColor.WHITE)
                        .append(text(" - Moves the NPC with the specified ID to your current location.")))
                .append(newline())
                .append(text("/npc delete <id>")
                        .color(NamedTextColor.WHITE)
                        .append(text(" - Deletes the NPC with the specified ID.")))
                .append(newline())
                .append(text("Example usage: /npc create MyNPC")
                        .color(NamedTextColor.GREEN)
                        .decorate(TextDecoration.ITALIC));

        sender.sendMessage(usageMessage);
    }

    private void handleCommand(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final String action = context.get(actionArgument);

        switch (action.toLowerCase()) {
            case "create" -> handleCreate(sender, context);
            case "move" -> handleMove(sender, context);
            case "delete" -> handleDelete(sender, context);
            default -> sender.sendMessage(Component.text("Invalid action. Use /npc help for usage instructions.")
                    .color(NamedTextColor.RED));
        }
    }

    private void handleDelete(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final int npcID = context.get(idArg);

        npcManager.deleteNPC(npcID);
    }

    private void handleMove(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final int npcID = context.get(idArg);

        npcManager.moveNPC(npcID, player.getPosition());
    }

    private void handleCreate(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String name = context.get(nameArg);
        final String skin = context.get(skinArg);

        npcManager.createNPC(name, Objects.requireNonNull(PlayerSkin.fromUsername(skin)), player.getInstance(), player.getPosition(), null);
    }

}