package cc.davyy.slime.commands.player;

import cc.davyy.slime.managers.TeleportManager;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.utils.entity.EntityFinder;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.GeneralUtils.hasPlayerPermission;

@Singleton
public class TeleportCommand extends Command {

    private final TeleportManager teleportManager;

    private final ArgumentEntity playerEntityArg = ArgumentType.Entity("player").onlyPlayers(true);

    @Inject
    public TeleportCommand(TeleportManager teleportManager) {
        super("tp");
        this.teleportManager = teleportManager;

        setCondition((sender, commandString) -> {
            if (!hasPlayerPermission(sender, "slime.teleport")) {
                sender.sendMessage(Messages.NO_PERMS.asComponent());
                return false;
            }
            return true;
        });

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