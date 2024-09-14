package cc.davyy.slime.commands;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Singleton;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.FileUtils.reloadConfig;
import static cc.davyy.slime.utils.GeneralUtils.hasPlayerPermission;

@Singleton
public class ConfigReloadCommand extends Command {

    public ConfigReloadCommand() {
        super("creload");

        setCondition((sender, commandString) -> {
            if (!hasPlayerPermission(sender, "slime.reload")) {
                sender.sendMessage(Messages.NO_PERMS.asComponent());
                return false;
            }
            return true;
        });

        setDefaultExecutor(this::reload);
    }

    private void reload(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        reloadConfig();
        player.sendMessage(Messages.RELOAD_CONFIG
                .asComponent());
    }

}