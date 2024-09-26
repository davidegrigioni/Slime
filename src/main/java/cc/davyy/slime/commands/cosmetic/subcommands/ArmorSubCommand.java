package cc.davyy.slime.commands.cosmetic.subcommands;

import cc.davyy.slime.model.cosmetics.ArmorData;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.cosmetics.ArmorCosmeticService;
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
public class ArmorSubCommand extends Command {

    private final ArmorCosmeticService armorCosmeticService;

    private final ArgumentLiteral createArg = ArgumentType.Literal("create");
    private final ArgumentLiteral applyArg = ArgumentType.Literal("apply");
    private final ArgumentLiteral deleteArg = ArgumentType.Literal("delete");

    private final ArgumentString nameArg = ArgumentType.String("name");
    private final ArgumentItemStack helmetArg = ArgumentType.ItemStack("helmet");
    private final ArgumentItemStack chestplateArg = ArgumentType.ItemStack("chestplate");
    private final ArgumentItemStack leggingsArg = ArgumentType.ItemStack("leggings");
    private final ArgumentItemStack bootsArg = ArgumentType.ItemStack("boots");
    private final ArgumentInteger idArg = ArgumentType.Integer("id");

    @Inject
    public ArmorSubCommand(ArmorCosmeticService armorCosmeticService) {
        super("armor");
        this.armorCosmeticService = armorCosmeticService;

        addSyntax(this::execute, createArg, nameArg, helmetArg, chestplateArg, leggingsArg, bootsArg);
        addSyntax(this::apply, applyArg, idArg);
        addSyntax(this::delete, deleteArg, idArg);
    }

    private void delete(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final int id = context.get(idArg);

        armorCosmeticService.removeCosmetic(player, id);
    }

    private void apply(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final int id = context.get(idArg);

        armorCosmeticService.applyCosmetic(player, id);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String name = context.get(nameArg);
        final ItemStack helmet = context.get(helmetArg);
        final ItemStack chest = context.get(chestplateArg);
        final ItemStack leggings = context.get(leggingsArg);
        final ItemStack boots = context.get(bootsArg);
        final ArmorData armorData = new ArmorData(helmet, chest, leggings, boots);

        armorCosmeticService.createCosmetic(of(name)
                .parseLegacy()
                .build(), armorData);

        player.sendMessage("created");
    }

}