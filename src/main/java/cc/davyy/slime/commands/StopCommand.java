package cc.davyy.slime.commands;

import com.asintoto.minestomacr.annotations.AutoRegister;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.Conditions;

@AutoRegister
public class StopCommand extends Command {

    public StopCommand() {
        super("stop");

        setCondition(Conditions::consoleOnly);

        setDefaultExecutor(((sender, context) -> MinecraftServer.stopCleanly()));
    }

}