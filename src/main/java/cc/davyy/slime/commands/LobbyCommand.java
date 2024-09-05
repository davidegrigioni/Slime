package cc.davyy.slime.commands;

import cc.davyy.slime.managers.LobbyManager;
import cc.davyy.slime.model.Lobby;
import com.google.inject.Inject;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LobbyCommand extends Command {

    private final LobbyManager lobbyManager;
    private final ArgumentLiteral createArg = ArgumentType.Literal("create");
    private final ArgumentLiteral teleportArg = ArgumentType.Literal("teleport");
    private final ArgumentInteger lobbyIDArg = ArgumentType.Integer("id");

    @Inject
    public LobbyCommand(LobbyManager lobbyManager) {
        super("lobby");
        this.lobbyManager = lobbyManager;

        addSyntax(this::execute, createArg);
        addSyntax(this::teleport, teleportArg, lobbyIDArg);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Lobby lobby = lobbyManager.createNewLobby();
        sender.sendMessage("Created new lobby " + lobby.name());
    }

    private void teleport(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        final int lobbyID = context.get(lobbyIDArg);
        lobbyManager.teleportPlayerToLobby(player, lobbyID);
    }

}