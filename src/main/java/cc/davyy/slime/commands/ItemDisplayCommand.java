package cc.davyy.slime.commands;

import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentItemStack;
import net.minestom.server.command.builder.arguments.relative.ArgumentRelativeVec3;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.ItemDisplayMeta;
import org.jetbrains.annotations.NotNull;

public class ItemDisplayCommand extends Command {

    private final ArgumentRelativeVec3 relativeArg = ArgumentType.RelativeVec3("scale");
    private final ArgumentItemStack itemArg = ArgumentType.ItemStack("item");
    private final ArgumentEnum<ItemDisplayMeta.DisplayContext> displayContextArg = ArgumentType.Enum("displayContext", ItemDisplayMeta.DisplayContext.class);
    private final ArgumentLiteral summonArg = ArgumentType.Literal("summon");

    private final Entity entity = new Entity(EntityType.ITEM_DISPLAY);

    public ItemDisplayCommand() {
        super("itemdisplay");

        addSyntax(this::summon, summonArg);
        addSyntax(this::modify, displayContextArg, itemArg, relativeArg);
    }

    private void modify(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final var displayMeta = (ItemDisplayMeta) entity.getEntityMeta();
        final var displayContext = context.get(displayContextArg);
        final var itemDisplay = context.get(itemArg);
        final var scaleDisplay = context.get(relativeArg);

        displayMeta.setScale(scaleDisplay.from((SlimePlayer) sender));
        displayMeta.setItemStack(itemDisplay);
        displayMeta.setDisplayContext(displayContext);
    }

    private void summon(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;

        entity.setInstance(player.getInstance(), player.getPosition());
    }

}