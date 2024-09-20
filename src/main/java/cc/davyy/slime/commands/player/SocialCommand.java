package cc.davyy.slime.commands.player;

import com.google.inject.Singleton;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cc.davyy.slime.utils.FileUtils.getConfig;
import static cc.davyy.slime.utils.ColorUtils.of;

@Singleton
public class SocialCommand extends Command {

    public SocialCommand() {
        super("social");

        setDefaultExecutor(this::execute);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final List<String> socials = getConfig().getStringList("socials");
        final String socialMessage = String.join("\n", socials);

        sender.sendMessage(of(socialMessage)
                .build());
    }

}