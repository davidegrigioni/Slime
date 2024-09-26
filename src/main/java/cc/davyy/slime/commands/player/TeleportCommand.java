package cc.davyy.slime.commands.player;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.TeleportService;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.relative.ArgumentRelativeVec3;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.utils.location.RelativeVec;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.GeneralUtils.hasPlayerPermission;

@Singleton
public class TeleportCommand extends Command {

    private final TeleportService teleportService;

    private final ArgumentLiteral toArg = ArgumentType.Literal("to");
    private final ArgumentLiteral toCoordsArg = ArgumentType.Literal("to-coords");

    private final ArgumentEntity targetEntityArg = ArgumentType.Entity("target").onlyPlayers(true);
    private final ArgumentRelativeVec3 posArg = ArgumentType.RelativeVec3("pos");
    private final ArgumentEntity playerEntityArg = ArgumentType.Entity("player").onlyPlayers(true);

    @Inject
    public TeleportCommand(TeleportService teleportService) {
        super("teleport","tp");
        this.teleportService = teleportService;

        setCondition((sender, commandString) -> {
            if (!hasPlayerPermission(sender, "slime.teleport")) {
                sender.sendMessage(Messages.NO_PERMS.asComponent());
                return false;
            }
            return true;
        });

        //addConditionalSyntax()
        addSyntax(this::onTeleport, toArg, playerEntityArg);
        addSyntax(this::onPosTeleport, toCoordsArg, posArg);
        addSyntax(this::onTargetToPlayerTeleport, targetEntityArg, playerEntityArg);
    }

    private void onPosTeleport(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final RelativeVec relativeVec = context.get(posArg);
        final Pos position = player.getPosition().withCoord(relativeVec.from(player));

        teleportService.teleportPlayerToCoordinates(player, position);
    }

    private void onTeleport(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final SlimePlayer slimePlayerTarget = (SlimePlayer) context.get(playerEntityArg)
                .findFirstPlayer(player);

        if (slimePlayerTarget != null) {
            teleportService.teleportPlayerToTarget(player, slimePlayerTarget);
        }

    }

    private void onTargetToPlayerTeleport(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer executor = (SlimePlayer) sender;
        final SlimePlayer target = (SlimePlayer) context.get(playerEntityArg)
                .findFirstPlayer(executor);

        teleportService.teleportTargetToExecutor(executor, target);
    }

}