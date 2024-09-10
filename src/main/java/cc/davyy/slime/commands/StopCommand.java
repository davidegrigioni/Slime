package cc.davyy.slime.commands;

import com.asintoto.minestomacr.annotations.AutoRegister;
import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.Conditions;

@AutoRegister
@Singleton
public class StopCommand extends Command {

    public StopCommand() {
        super("stop");

        setCondition(Conditions::consoleOnly);

        setDefaultExecutor(((sender, context) -> MinecraftServer.stopCleanly()));
    }

}