package cc.davyy.slime.commands;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.DisguiseService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.registry.ArgumentEntityType;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

@Singleton
public class DisguiseCommand extends Command {

    private final DisguiseService disguiseService;
    private final ArgumentLiteral undisguiseNickArg = ArgumentType.Literal("udn");
    private final ArgumentLiteral undisguiseEntityArg = ArgumentType.Literal("ude");
    private final ArgumentEntityType entityTypeArg = ArgumentType.EntityType("entity");
    private final ArgumentString nickNameArg = ArgumentType.String("nickname");

    @Inject
    public DisguiseCommand(DisguiseService disguiseService) {
        super("disguise");
        this.disguiseService = disguiseService;

        addSyntax(this::undisguiseNick, undisguiseNickArg);
        addSyntax(this::undisguiseEntity, undisguiseEntityArg);
        addSyntax(this::disguiseEntity, entityTypeArg);
        addSyntax(this::disguiseNick, nickNameArg);
    }

    private void undisguiseNick(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;

        disguiseService.removeNicknamedDisguise(player);
    }

    private void undisguiseEntity(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;

        disguiseService.removeEntityDisguise(player);
    }

    private void disguiseNick(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String nickName = context.get(nickNameArg);

        disguiseService.setNicknamedDisguise(player, nickName);
    }

    private void disguiseEntity(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final EntityType entityType = context.get(entityTypeArg);

        disguiseService.setEntityDisguise(player, entityType);
    }

}