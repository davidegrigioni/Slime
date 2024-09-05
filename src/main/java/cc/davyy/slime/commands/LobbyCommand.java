package cc.davyy.slime.commands;

import cc.davyy.slime.managers.LobbyManager;
import cc.davyy.slime.model.Lobby;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Inject;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.ColorUtils.print;

public class LobbyCommand extends Command {

    private final LobbyManager lobbyManager;

    private final ArgumentLiteral createArg = ArgumentType.Literal("create");
    private final ArgumentLiteral teleportArg = ArgumentType.Literal("teleport");
    private final ArgumentInteger lobbyIDArg = ArgumentType.Integer("id");

    @Inject
    public LobbyCommand(LobbyManager lobbyManager) {
        super("lobby");
        this.lobbyManager = lobbyManager;

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