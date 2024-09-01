package cc.davyy.slime.commands;

import cc.davyy.slime.utils.Messages;
import com.asintoto.minestomacr.annotations.AutoRegister;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.FileUtils.reloadConfig;

@AutoRegister
public class ConfigReloadCommand extends Command {

    public ConfigReloadCommand() {
        super("creload");

        setCondition(((sender, commandString) -> {
            if (sender instanceof Player player) {
                return player.hasPermission("slime.reload");
            }
            return true;
        }));

        setDefaultExecutor(this::reload);
    }

    private void reload(@NotNull CommandSender sender, @NotNull CommandContext context) {
        reloadConfig();
        sender.sendMessage(Messages.RELOAD_CONFIG
                .asComponent());
    }

}