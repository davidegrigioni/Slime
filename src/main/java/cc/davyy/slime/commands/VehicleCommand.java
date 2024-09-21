package cc.davyy.slime.commands;

import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Singleton;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.registry.ArgumentEntityType;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

@Singleton
public class VehicleCommand extends Command {

    private EntityCreature entityCreature;

    private final ArgumentEntityType entityTypeArg = ArgumentType.EntityType("entitytype");

    private final ArgumentLiteral removeArg = ArgumentType.Literal("remove");
    private final ArgumentLiteral rideArg = ArgumentType.Literal("ride");

    public VehicleCommand() {
        super("vehicle");

        addSyntax(this::createVehicle, entityTypeArg);
        addSyntax(this::mountVehicle, rideArg);
        addSyntax(this::removeVehicle, removeArg);
    }

    private void removeVehicle(@NotNull CommandSender sender, @NotNull CommandContext context) {
        entityCreature.remove();
    }

    private void mountVehicle(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        entityCreature.addPassenger(player);
        player.sendMessage("ride entity");
    }

    private void createVehicle(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final EntityType entityType = context.get(entityTypeArg);

        entityCreature = new EntityCreature(entityType);
        entityCreature.setInstance(player.getInstance(), player.getPosition());
        player.sendMessage("created vehicle");
    }

}