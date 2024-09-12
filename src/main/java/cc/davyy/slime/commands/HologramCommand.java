package cc.davyy.slime.commands;

import cc.davyy.slime.managers.HologramManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.ColorUtils.of;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.TextColor.color;

@Singleton
public class HologramCommand extends Command {

    private final HologramManager hologramManager;

    private final ArgumentWord argumentWord = ArgumentType.Word("args")
            .from("create", "delete", "teleport");

    private final ArgumentString textArg = ArgumentType.String("text");

    @Inject
    public HologramCommand(HologramManager hologramManager) {
        super("hologram", "holo");
        this.hologramManager = hologramManager;

        setDefaultExecutor(((commandSender, commandContext) -> {
            final String usage = """
                    /hologram create [text]\s
                    /hologram move <id>\s
                    /hologram delete <id>""";

            final Component usageMessage = text("Usage Instructions:")
                    .color(color(255, 0, 0))
                    .append(newline())
                    .append(text(usage)
                            .color(color(255, 255, 255)));

            commandSender.sendMessage(usageMessage);
        }));

        addSyntax(this::execute, argumentWord);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String text = context.get(textArg);

        hologramManager.createHologram(player, of(text).parseLegacy().build());
    }

}