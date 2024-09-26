package cc.davyy.slime.commands.player;

import cc.davyy.slime.managers.general.ConfigManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cc.davyy.slime.utils.ColorUtils.of;

@Singleton
public class SocialCommand extends Command {

    private final ConfigManager configManager;

    @Inject
    public SocialCommand(ConfigManager configManager) {
        super("social");
        this.configManager = configManager;

        setDefaultExecutor(this::execute);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final List<String> socials = configManager.getConfig().getStringList("socials");
        final String socialMessage = String.join("\n", socials);

        sender.sendMessage(of(socialMessage)
                .build());
    }

}