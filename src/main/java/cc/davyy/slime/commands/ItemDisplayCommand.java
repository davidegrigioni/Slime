package cc.davyy.slime.commands;

import cc.davyy.slime.managers.ItemDisplayManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentItemStack;
import net.minestom.server.command.builder.arguments.relative.ArgumentRelativeVec3;
import net.minestom.server.entity.metadata.display.ItemDisplayMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.location.RelativeVec;
import org.jetbrains.annotations.NotNull;

@Singleton
public class ItemDisplayCommand extends Command {

    private final ArgumentRelativeVec3 relativeArg = ArgumentType.RelativeVec3("scale");
    private final ArgumentItemStack itemArg = ArgumentType.ItemStack("item");
    private final ArgumentEnum<ItemDisplayMeta.DisplayContext> displayContextArg = ArgumentType.Enum("displayContext", ItemDisplayMeta.DisplayContext.class);
    private final ArgumentLiteral summonArg = ArgumentType.Literal("summon");

    private final ItemDisplayManager itemDisplayManager;

    @Inject
    public ItemDisplayCommand(ItemDisplayManager itemDisplayManager) {
        super("itemdisplay");
        this.itemDisplayManager = itemDisplayManager;

        addSyntax(this::summon, summonArg);
        addSyntax(this::modify, displayContextArg, itemArg, relativeArg);
    }

    private void summon(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;

        itemDisplayManager.summonItemDisplay(player);
    }

    private void modify(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final ItemStack itemDisplay = context.get(itemArg);
        final RelativeVec scale = context.get(relativeArg);
        final ItemDisplayMeta.DisplayContext displayContext = context.get(displayContextArg);

        itemDisplayManager.modifyItemDisplay(player, itemDisplay, scale, displayContext);
    }

}