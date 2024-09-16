package cc.davyy.slime.commands.cosmetic.subcommands;

import cc.davyy.slime.cosmetics.managers.HatCosmeticManager;
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
import net.minestom.server.command.builder.arguments.minecraft.ArgumentItemStack;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.ColorUtils.of;

@Singleton
public class HatCosmeticSubCommand extends Command {

    private final HatCosmeticManager hatCosmeticManager;

    private final ArgumentLiteral createArg = ArgumentType.Literal("create");
    private final ArgumentLiteral applyArg = ArgumentType.Literal("apply");
    private final ArgumentLiteral deleteArg = ArgumentType.Literal("delete");

    private final ArgumentString nameArg = ArgumentType.String("name");
    private final ArgumentItemStack itemStackArg = ArgumentType.ItemStack("item");
    private final ArgumentInteger idArg = ArgumentType.Integer("id");

    @Inject
    public HatCosmeticSubCommand(HatCosmeticManager hatCosmeticManager) {
        super("item");
        this.hatCosmeticManager = hatCosmeticManager;

        addSyntax(this::execute, createArg, nameArg, itemStackArg);
        addSyntax(this::apply, applyArg, idArg);
        addSyntax(this::delete, deleteArg, idArg);
    }

    private void delete(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final int id = context.get(idArg);

        hatCosmeticManager.removeCosmetic(player, id);
    }

    private void apply(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final int id = context.get(idArg);

        hatCosmeticManager.applyCosmetic(player, id);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String name = context.get(nameArg);
        final ItemStack item = context.get(itemStackArg);

        hatCosmeticManager.createCosmetic(of(name)
                .parseLegacy()
                .build(), item);

        player.sendMessage(Messages.ITEM_COSMETIC_CREATED
                .addPlaceholder("name", name)
                .addPlaceholder("item", item.material().name())
                .asComponent());
    }

}