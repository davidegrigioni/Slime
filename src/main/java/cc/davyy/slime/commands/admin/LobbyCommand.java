package cc.davyy.slime.commands.admin;

import cc.davyy.slime.managers.LobbyManager;
import cc.davyy.slime.model.Lobby;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.minestom.server.command.builder.exception.ArgumentSyntaxException;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.GeneralUtils.hasPlayerPermission;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.TextColor.color;

@Singleton
public class LobbyCommand extends Command {

    private final LobbyManager lobbyManager;

    private final ArgumentNumber<Integer> lobbyIDArg = ArgumentType.Integer("id").between(0, 50);

    private final ArgumentLiteral createArg = ArgumentType.Literal("create");
    private final ArgumentLiteral teleportArg = ArgumentType.Literal("teleport");

    @Inject
    public LobbyCommand(LobbyManager lobbyManager) {
        super("lobby");
        this.lobbyManager = lobbyManager;

        setCondition((sender, commandString) -> {
            if (!hasPlayerPermission(sender, "slime.lobby")) {
                sender.sendMessage(Messages.NO_PERMS.asComponent());
                return false;
            }
            return true;
        });

        setDefaultExecutor(this::showUsage);

        setArgumentCallback(this::onSyntaxError, lobbyIDArg);

        addSyntax(this::create, createArg);
        addSyntax(this::handleTeleport, teleportArg, lobbyIDArg);
    }

    private void onSyntaxError(@NotNull CommandSender sender, @NotNull ArgumentSyntaxException exception) {
        final int error = exception.getErrorCode();
        final String input = exception.getInput();
        switch (error) {
            case ArgumentNumber.NOT_NUMBER_ERROR:
                sender.sendMessage(Component.text("SYNTAX ERROR: '" + input + "' isn't a number!"));
                break;
            case ArgumentNumber.TOO_LOW_ERROR:
            case ArgumentNumber.TOO_HIGH_ERROR:
                sender.sendMessage(Component.text("SYNTAX ERROR: " + input + " is not between 0 and 100"));
                break;
        }
    }

    private void showUsage(@NotNull CommandSender commandSender, @NotNull CommandContext context) {
        final Component usageMessage = text("Usage Instructions:")
                .color(color(255, 0, 0))
                .append(newline())
                .append(text("/lobby create")
                        .color(color(255, 255, 255))
                        .append(text(" - Creates a new lobby.")))
                .append(newline())
                .append(text("/lobby teleport <id>")
                        .color(color(255, 255, 255))
                        .append(text(" - Teleports you to the lobby with the specified ID.")))
                .append(newline())
                .append(text("Example usage: /lobby teleport 1")
                        .color(color(100, 200, 100))
                        .decorate(TextDecoration.ITALIC));

        commandSender.sendMessage(usageMessage);
    }

    private void create(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final Lobby lobby = lobbyManager.createNewLobby();

        player.sendMessage(Messages.LOBBY_CREATED
                .addPlaceholder("lobbyname", lobby.name())
                .asComponent());
    }

    private void handleTeleport(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final int lobbyID = context.get(lobbyIDArg);

        lobbyManager.teleportPlayerToLobby(player, lobbyID);
    }

}