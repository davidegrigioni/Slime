package cc.davyy.slime.commands;

import cc.davyy.slime.managers.RegionManager;
import cc.davyy.slime.model.Region;
import com.asintoto.minestomacr.annotations.AutoRegister;
import com.google.inject.Inject;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

public class RegionCommand extends Command {

    private final RegionManager regionManager;

    @Inject
    public RegionCommand(RegionManager regionManager) {
        super("region");
        this.regionManager = regionManager;

        setCondition(((sender, commandString) -> {
            final Player player = (Player) sender;
            return player.hasPermission("slime.region");
        }));

        var regionNameArg = ArgumentType.String("regionName");
        addSyntax(this::createRegion, regionNameArg);

        var pointArg = ArgumentType.Integer("point").between(1, 2);
        addSyntax(this::setRegion, pointArg);

        var saveArg = ArgumentType.Literal("save");
        addSyntax(this::saveRegion, saveArg, regionNameArg);

        var cancelArg = ArgumentType.Literal("cancel");
        addSyntax(this::cancelSetup, cancelArg);
    }

    private void createRegion(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        String regionName = context.get("regionName");
        regionManager.startRegionSetup(player.getUuid(), regionName);
        player.sendMessage("Region setup started for '" + regionName + "'. Please set the points using /region set 1 and /region set 2.");
    }

    private void setRegion(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        int pointNumber = context.get("point");
        Vec playerPosition = player.getPosition().asVec();

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
        String regionName = context.get("regionName");

        if (!regionManager.isSettingUpRegion(player.getUuid())) {
            player.sendMessage("No region setup in progress. Start one using /region create <regionName>.");
            return;
        }

        Region region = regionManager.saveRegion(player.getUuid());
        Check.notNull(region, "Failed to save the region. Ensure both points are set.");

        player.sendMessage("Region '" + regionName + "' saved successfully between " + region.getMinPoint() + " and " + region.getMaxPoint());
    }

    private void cancelSetup(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        regionManager.cancelRegionSetup(player.getUuid());
        player.sendMessage("Region setup cancelled.");
    }

}