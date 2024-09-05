package cc.davyy.slime.commands;

import cc.davyy.slime.managers.RegionManager;
import cc.davyy.slime.model.Region;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Inject;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static cc.davyy.slime.utils.ColorUtils.print;

public class RegionCommand extends Command {

    private final ArgumentString regionNameArg = ArgumentType.String("regionName");
    private final ArgumentInteger pointArg = ArgumentType.Integer("point");
    private final ArgumentLiteral saveArg = ArgumentType.Literal("save");
    private final ArgumentLiteral cancelArg = ArgumentType.Literal("cancel");

    private final RegionManager regionManager;

    @Inject
    public RegionCommand(RegionManager regionManager) {
        super("region");
        this.regionManager = regionManager;

        setCondition(((sender, commandString) -> switch (sender) {
            case SlimePlayer player -> player.hasPermission("slime.region");
            case ConsoleSender ignored -> true;
            default -> false;
        }));

        addSyntax(this::createRegion, regionNameArg);
        addSyntax(this::setRegion, pointArg);
        addSyntax(this::saveRegion, saveArg, regionNameArg);
        addSyntax(this::cancelSetup, cancelArg);
    }

    private void createRegion(@NotNull CommandSender sender, @NotNull CommandContext context) {
        switch (sender) {
            case SlimePlayer player -> {
                final String regionName = context.get(regionNameArg);
                regionManager.startRegionSetup(player.getUuid(), regionName);

                player.sendMessage(Messages.REGION_SETUP
                        .addPlaceholder("regionname", regionName)
                        .asComponent());
            }
            case ConsoleSender ignored -> print(Messages.CANNOT_EXECUTE_FROM_CONSOLE
                    .asComponent());
            default -> {}
        }
    }

    private void setRegion(@NotNull CommandSender sender, @NotNull CommandContext context) {
        switch (sender) {
            case SlimePlayer player -> {
                final int pointNumber = context.get(pointArg);
                final Vec playerPosition = player.getPosition().asVec();

                if (!regionManager.isSettingUpRegion(player.getUuid())) {
                    player.sendMessage(Messages.REGION_SETUP_FIRST
                            .asComponent());
                    return;
                }

                switch (pointNumber) {
                    case 1 -> {
                        regionManager.setPoint1(player.getUuid(), playerPosition);
                        player.sendMessage("First point set at " + playerPosition);
                    }
                    case 2 -> {
                        regionManager.setPoint2(player.getUuid(), playerPosition);
                        player.sendMessage("Second point set at " + playerPosition);
                    }
                }
            }
            case ConsoleSender ignored -> print(Messages.CANNOT_EXECUTE_FROM_CONSOLE
                    .asComponent());
            default -> {}
        }
    }

    private void saveRegion(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        final String regionName = context.get(regionNameArg);

        if (!regionManager.isSettingUpRegion(player.getUuid())) {
            player.sendMessage(Messages.REGION_SETUP_FIRST
                    .asComponent());
            return;
        }

        final Optional<Region> regionOpt = regionManager.saveRegion(player.getUuid());

        regionOpt.ifPresentOrElse(region -> player.sendMessage("Region '" + regionName + "' saved successfully between "
                + region.getMinPoint() + " and " + region.getMaxPoint()), () -> {
            throw new IllegalStateException("Failed to save the region. Ensure both points are set.");
        });
    }

    private void cancelSetup(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        regionManager.cancelRegionSetup(player.getUuid());
        player.sendMessage("Region setup cancelled.");
    }

}