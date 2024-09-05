package cc.davyy.slime.commands;

import cc.davyy.slime.gui.ServerGUI;
import cc.davyy.slime.managers.LobbyManager;
import com.google.inject.Inject;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static cc.davyy.slime.utils.ColorUtils.of;

public class DebugCommand extends Command {

    private final LobbyManager lobbyManager;

    @Inject
    public DebugCommand(LobbyManager lobbyManager) {
        super("debug");
        this.lobbyManager = lobbyManager;

        addSyntax(this::execute, ArgumentType.Literal("brandname"));
        addSyntax(this::executeInstances, ArgumentType.Literal("instances"));
        addSyntax(this::executeMainInstanceCheck, ArgumentType.Literal("ismaininstance"));
        addSyntax(this::gui, ArgumentType.Literal("servergui"));
    }

    private void gui(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        new ServerGUI().open(player);
    }

    private void executeMainInstanceCheck(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        final boolean isMain = lobbyManager.isMainInstance(player.getInstance());
        player.sendMessage(isMain ? "You are currently in the main lobby instance." : "You are currently in a shared lobby instance.");
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        String brandName = MinecraftServer.getBrandName();
        player.sendMessage(of(brandName)
                .parseLegacy()
                .build());
    }

    private void executeInstances(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        final Collection<Integer> lobbyIDs = lobbyManager.getAllLobbiesID();
        player.sendMessage(lobbyIDs.toString());
    }

}