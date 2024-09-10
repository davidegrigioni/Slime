package cc.davyy.slime.commands;

import cc.davyy.slime.managers.HologramManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.ColorUtils.of;

@Singleton
public class HologramCommand extends Command {

    private final HologramManager hologramManager;

    private final ArgumentLiteral createArg = ArgumentType.Literal("create");
    private final ArgumentString textArg = ArgumentType.String("text");

    @Inject
    public HologramCommand(HologramManager hologramManager) {
        super("hologram", "holo");
        this.hologramManager = hologramManager;

        addSyntax(this::createHolo, createArg, textArg);
    }

    private void createHolo(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String text = context.get(textArg);

        hologramManager.createHologram(player, of(text).parseLegacy().build());
    }

}