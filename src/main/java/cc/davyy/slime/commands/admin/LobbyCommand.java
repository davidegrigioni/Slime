package cc.davyy.slime.commands.admin;

import cc.davyy.slime.model.Lobby;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.gameplay.LobbyService;
import cc.davyy.slime.model.Messages;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.TextColor.color;

@Command(name = "lobby")
@Permission("slime.lobby")
@Singleton
public class LobbyCommand {

    private final LobbyService lobbyService;

    @Inject
    public LobbyCommand(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @Execute
    void showUsage(@Context CommandSender sender) {
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

        sender.sendMessage(usageMessage);
    }

    @Execute(name = "create")
    void create(@Context CommandSender sender) {
        final Lobby lobby = lobbyService.createNewLobby();

        sender.sendMessage(Messages.LOBBY_CREATED
                .addPlaceholder("lobbyname", lobby.name())
                .asComponent());
    }

    @Execute(name = "teleport")
    void handleTeleport(@Context SlimePlayer player, @Arg int lobbyID) {
        lobbyService.teleportPlayerToLobby(player, lobbyID);
    }

}