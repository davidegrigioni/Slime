package cc.davyy.slime.commands.admin;

import cc.davyy.slime.managers.ConfigManager;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.GeneralUtils.hasPlayerPermission;

@Singleton
public class SlimeCommand extends Command {

    private final ConfigManager configManager;

    private final ArgumentLiteral reloadArg = ArgumentType.Literal("reload");

    @Inject
    public SlimeCommand(ConfigManager configManager) {
        super("slime");
        this.configManager = configManager;

        setCondition((sender, commandString) -> {
            if (!hasPlayerPermission(sender, "slime.reload")) {
                sender.sendMessage(Messages.NO_PERMS.asComponent());
                return false;
            }
            return true;
        });

        addSyntax(this::reload, reloadArg);
    }

    private void reload(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;

        configManager.reload();

        player.sendMessage(Messages.RELOAD_CONFIG
                .asComponent());
    }

}