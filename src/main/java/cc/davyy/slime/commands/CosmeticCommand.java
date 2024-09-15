package cc.davyy.slime.commands;

import cc.davyy.slime.cosmetics.managers.ItemCosmeticManager;
import cc.davyy.slime.cosmetics.model.Cosmetic;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentItemStack;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CosmeticCommand extends Command {

    @Inject
    public CosmeticCommand(ItemCosmeticManager itemCosmeticManager) {
        super("cosmetic");

        addSubcommand(new ItemCosmeticSubCommand(itemCosmeticManager));
    }

    private static class ItemCosmeticSubCommand extends Command {

        private final ArgumentString nameArg = ArgumentType.String("name");
        private final ArgumentInteger idArg = ArgumentType.Integer("id");
        private final ArgumentItemStack itemArg = ArgumentType.ItemStack("item");
        private final ArgumentEnum<Cosmetic.CosmeticType> typeArg = ArgumentType.Enum("type", Cosmetic.CosmeticType.class);

        private final ArgumentLiteral createArg = ArgumentType.Literal("create");
        private final ArgumentLiteral applyArg = ArgumentType.Literal("apply");

        private final ItemCosmeticManager itemCosmeticManager;

        @Inject
        public ItemCosmeticSubCommand(ItemCosmeticManager itemCosmeticManager) {
            super("item");
            this.itemCosmeticManager = itemCosmeticManager;

            addSyntax(this::executeCreate, createArg, nameArg, itemArg, typeArg);
            addSyntax(this::executeApply, applyArg, idArg);
        }

        private void executeCreate(@NotNull CommandSender sender, @NotNull CommandContext context) {
            final String name = context.get(nameArg);
            final ItemStack item = context.get(itemArg);
            final Cosmetic.CosmeticType type = context.get(typeArg);

            itemCosmeticManager.createCosmetic(name, item, type);
            sender.sendMessage("Created cosmetic: " + name);
        }

        private void executeApply(@NotNull CommandSender sender, @NotNull CommandContext context) {
            final SlimePlayer player = (SlimePlayer) sender;
            final int id = context.get(idArg);

            itemCosmeticManager.applyCosmetic(player, id);
        }

    }

}