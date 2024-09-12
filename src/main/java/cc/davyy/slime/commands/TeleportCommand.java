package cc.davyy.slime.commands;

import cc.davyy.slime.managers.TeleportManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.exception.ArgumentSyntaxException;
import org.jetbrains.annotations.NotNull;

@Singleton
public class TeleportCommand extends Command {

    private final TeleportManager teleportManager;

    private final ArgumentWord playerArg = ArgumentType.Word("player");

    @Inject
    public TeleportCommand(TeleportManager teleportManager) {
        super("tp");
        this.teleportManager = teleportManager;

        setArgumentCallback(this::onPlayerError, playerArg);

        addSyntax(this::onTeleport, playerArg);
    }

    private void onPlayerError(@NotNull CommandSender sender, @NotNull ArgumentSyntaxException exception) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String input = exception.getInput();

        player.sendMessage("SYNTAX ERROR: " + input + " is not valid.");
    }

    private void onTeleport(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String playerName = context.get(playerArg);
        final SlimePlayer slimePlayerTarget = (SlimePlayer) MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(playerName);

        if (slimePlayerTarget != null) {
            teleportManager.teleportPlayerToTarget(player, slimePlayerTarget);
        }

    }

}