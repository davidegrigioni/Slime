package cc.davyy.slime.commands.admin;

import com.google.inject.Singleton;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;

@Command(name = "stop")
@Permission("slime.stop")
@Singleton
public class StopCommand {

    @Execute
    void execute(@Context CommandSender sender) {
        MinecraftServer.stopCleanly();
    }

}