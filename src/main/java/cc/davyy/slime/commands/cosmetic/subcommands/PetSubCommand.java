package cc.davyy.slime.commands.cosmetic.subcommands;

import cc.davyy.slime.managers.cosmetics.PetCosmeticManager;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.registry.ArgumentEntityType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.ColorUtils.of;

@Singleton
public class PetSubCommand extends Command {

    private final PetCosmeticManager petCosmeticManager;

    private final ArgumentLiteral createArg = ArgumentType.Literal("create");
    private final ArgumentLiteral applyArg = ArgumentType.Literal("apply");
    private final ArgumentLiteral deleteArg = ArgumentType.Literal("delete");

    private final ArgumentString nameArg = ArgumentType.String("name");
    private final ArgumentEntityType entityArg = ArgumentType.EntityType("entitytype");
    private final ArgumentInteger idArg = ArgumentType.Integer("id");

    @Inject
    public PetSubCommand(PetCosmeticManager petCosmeticManager) {
        super("pet");
        this.petCosmeticManager = petCosmeticManager;

        addSyntax(this::execute, createArg, nameArg, entityArg);
        addSyntax(this::apply, applyArg, idArg);
        addSyntax(this::delete, deleteArg, idArg);
    }

    private void delete(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final int id = context.get(idArg);

        petCosmeticManager.removeCosmetic(player, id);
    }

    private void apply(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final int id = context.get(idArg);

        petCosmeticManager.applyCosmetic(player, id);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String name = context.get(nameArg);
        final EntityType entityType = context.get(entityArg);

        petCosmeticManager.createCosmetic(of(name)
                .parseLegacy()
                .build(), entityType);

        player.sendMessage(Messages.PET_COSMETIC_CREATED
                .addPlaceholder("name", name)
                .addPlaceholder("entity", entityType.name())
                .asComponent());
    }

}