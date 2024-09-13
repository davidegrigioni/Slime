package cc.davyy.slime.commands;

import cc.davyy.slime.managers.TeleportManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.utils.entity.EntityFinder;
import org.jetbrains.annotations.NotNull;

@Singleton
public class TeleportCommand extends Command {

    private final TeleportManager teleportManager;

    private final ArgumentEntity playerEntityArg = ArgumentType.Entity("player");

    @Inject
    public TeleportCommand(TeleportManager teleportManager) {
        super("tp");
        this.teleportManager = teleportManager;

        addSyntax(this::onTeleport, playerEntityArg);
    }

    private void onTeleport(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final EntityFinder entityName = context.get(playerEntityArg);
        final SlimePlayer slimePlayerTarget = (SlimePlayer) entityName.findFirstPlayer(player);

        if (slimePlayerTarget != null) {
            teleportManager.teleportPlayerToTarget(player, slimePlayerTarget);
        }

    }

}