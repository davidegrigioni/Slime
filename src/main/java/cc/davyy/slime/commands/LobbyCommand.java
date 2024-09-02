package cc.davyy.slime.commands;

import cc.davyy.slime.managers.LobbyManager;
import cc.davyy.slime.model.Lobby;
import com.google.inject.Inject;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.jetbrains.annotations.NotNull;

public class LobbyCommand extends Command {

    private final LobbyManager lobbyManager;
    private final ArgumentLiteral createArg = ArgumentType.Literal("create");

    @Inject
    public LobbyCommand(LobbyManager lobbyManager) {
        super("lobby");
        this.lobbyManager = lobbyManager;

        setCondition(((sender, commandString) -> sender.hasPermission("slime.lobby")));

        addSyntax(this::execute, createArg);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Lobby lobby = lobbyManager.createNewLobby();
        sender.sendMessage("Created new lobby " + lobby.name());
    }

}