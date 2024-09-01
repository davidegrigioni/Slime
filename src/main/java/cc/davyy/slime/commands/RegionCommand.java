package cc.davyy.slime.commands;

import cc.davyy.slime.managers.RegionManager;
import cc.davyy.slime.model.Region;
import com.google.inject.Inject;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RegionCommand extends Command {

    private final ArgumentString regionNameArg = ArgumentType.String("regionName");
    private final ArgumentInteger pointArg = ArgumentType.Integer("point");

    private final RegionManager regionManager;

    @Inject
    public RegionCommand(RegionManager regionManager) {
        super("region");
        this.regionManager = regionManager;

        setCondition(((sender, commandString) -> {
            final Player player = (Player) sender;
            return player.hasPermission("slime.region");
        }));

        addSyntax(this::createRegion, regionNameArg);

        addSyntax(this::setRegion, pointArg);

        var saveArg = ArgumentType.Literal("save");
        addSyntax(this::saveRegion, saveArg, regionNameArg);

        var cancelArg = ArgumentType.Literal("cancel");
        addSyntax(this::cancelSetup, cancelArg);
    }

    private void createRegion(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        final String regionName = context.get(regionNameArg);
        regionManager.startRegionSetup(player.getUuid(), regionName);
        player.sendMessage("Region setup started for '" + regionName + "'. Please set the points using /region set 1 and /region set 2.");
    }

    private void setRegion(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        final int pointNumber = context.get(pointArg);
        final Vec playerPosition = player.getPosition().asVec();

        if (!regionManager.isSettingUpRegion(player.getUuid())) {
            player.sendMessage("You need to start a region setup first using /region create <regionName>.");
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

    private void saveRegion(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        final String regionName = context.get(regionNameArg);

        if (!regionManager.isSettingUpRegion(player.getUuid())) {
            player.sendMessage("No region setup in progress. Start one using /region create <regionName>.");
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