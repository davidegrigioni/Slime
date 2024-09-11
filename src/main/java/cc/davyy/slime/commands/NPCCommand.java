package cc.davyy.slime.commands;

import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.TextColor.color;

@Singleton
public class NPCCommand extends Command {

    public NPCCommand() {
        super("npc");

        setDefaultExecutor(((commandSender, commandContext) -> {
            final String usage = """
                    /npc create [npcname]\s
                    /npc move <id>\s
                    /npc delete <id>""";

            final Component usageMessage = text("Usage Instructions:")
                    .color(color(255, 0, 0))
                    .append(newline())
                    .append(text(usage)
                            .color(color(255, 255, 255)));

            commandSender.sendMessage(usageMessage);
        }));

    }

}