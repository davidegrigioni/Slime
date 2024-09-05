package cc.davyy.slime.commands;

import com.asintoto.minestomacr.annotations.AutoRegister;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;

@AutoRegister
public class StopCommand extends Command {

    public StopCommand() {
        super("stop");

        setDefaultExecutor(((sender, context) -> MinecraftServer.stopCleanly()));
    }

}