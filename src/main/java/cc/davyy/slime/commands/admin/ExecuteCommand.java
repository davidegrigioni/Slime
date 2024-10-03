package cc.davyy.slime.commands.admin;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.model.Messages;
import com.google.inject.Singleton;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import net.minestom.server.MinecraftServer;

@Command(name = "execute")
@Permission("slime.execute")
@Singleton
public class ExecuteCommand {

    @Execute
    void execute(@Context SlimePlayer player, @Arg SlimePlayer target, @Arg String command) {
        MinecraftServer.getCommandManager().execute(target, command);

        player.sendMessage(Messages.EXECUTE_COMMAND_MESSAGE
                .addPlaceholder("command", command)
                .addPlaceholder("target", player.getUsername())
                .asComponent());
    }

}