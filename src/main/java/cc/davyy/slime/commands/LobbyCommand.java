package cc.davyy.slime.commands;

import cc.davyy.slime.managers.LobbyManager;
import cc.davyy.slime.model.Lobby;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.ColorUtils.print;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;

public class LobbyCommand extends Command {

    private final LobbyManager lobbyManager;

    private final ArgumentLiteral createArg = ArgumentType.Literal("create");
    private final ArgumentLiteral teleportArg = ArgumentType.Literal("teleport");
    private final ArgumentInteger lobbyIDArg = ArgumentType.Integer("id");

    @Inject
    public LobbyCommand(LobbyManager lobbyManager) {
        super("lobby");
        this.lobbyManager = lobbyManager;

        setDefaultExecutor(((commandSender, commandContext) -> {
            Component usageMessage = text("Usage Instructions:")
                    .color(TextColor.color(255, 0, 0))
                    .append(newline())
                    .append(text("/lobby create")
                            .color(TextColor.color(255, 255, 255))
                            .append(text(" - Creates a new lobby.")))
                    .append(newline())
                    .append(text("/lobby teleport <id>")
                            .color(TextColor.color(255, 255, 255))
                            .append(text(" - Teleports you to the lobby with the specified ID.")))
                    .append(newline())
                    .append(text("Example usage: /lobby teleport 1")
                            .color(TextColor.color(100, 200, 100))
                            .decorate(TextDecoration.ITALIC));

            commandSender.sendMessage(usageMessage);
        }));

        setCondition(((sender, commandString) -> switch (sender) {
            case SlimePlayer player -> player.hasPermission("slime.lobby");
            case ConsoleSender ignored -> true;
            default -> false;
        }));

        addSyntax(this::execute, createArg);
        addSyntax(this::teleport, teleportArg, lobbyIDArg);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Lobby lobby = lobbyManager.createNewLobby();
        sender.sendMessage("Created new lobby " + lobby.name());
    }

    private void teleport(@NotNull CommandSender sender, @NotNull CommandContext context) {
        switch (sender) {
            case SlimePlayer player -> {
                final int lobbyID = context.get(lobbyIDArg);
                lobbyManager.teleportPlayerToLobby(player, lobbyID);
            }
            case ConsoleSender ignored -> print(Messages.CANNOT_EXECUTE_FROM_CONSOLE
                    .asComponent());
            default -> {}
        }
    }

}