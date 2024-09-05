package cc.davyy.slime.commands;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import com.asintoto.minestomacr.annotations.AutoRegister;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.ColorUtils.print;
import static cc.davyy.slime.utils.FileUtils.reloadConfig;

@AutoRegister
public class ConfigReloadCommand extends Command {

    public ConfigReloadCommand() {
        super("creload");

        setCondition(((sender, commandString) -> switch (sender) {
            case SlimePlayer player -> player.hasPermission("slime.reload");
            case ConsoleSender ignored -> true;
            default -> false;
        }));

        setDefaultExecutor(this::reload);
    }

    private void reload(@NotNull CommandSender sender, @NotNull CommandContext context) {
        reloadConfig();

        switch (sender) {
            case ConsoleSender ignored -> print(Messages.RELOAD_CONFIG
                    .asComponent());
            case Player player -> player.sendMessage(Messages.RELOAD_CONFIG
                    .asComponent());
            default -> {}
        }
    }

}