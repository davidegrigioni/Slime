package cc.davyy.slime.commands.admin;

import cc.davyy.slime.managers.general.ConfigManager;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import net.minestom.server.command.CommandSender;

@Command(name = "slime")
@Permission("slime.core")
@Singleton
public class SlimeCommand {

    private final ConfigManager configManager;

    @Inject
    public SlimeCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Execute(name = "reload")
    void reload(@Context CommandSender sender) {
        configManager.reload();

        sender.sendMessage(Messages.RELOAD_CONFIG
                .asComponent());
    }

}