package cc.davyy.slime.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.Conditions;

@Singleton
public class StopCommand extends Command {

    @Inject
    public StopCommand() {
        super("stop");

        setCondition(Conditions::consoleOnly);

        setDefaultExecutor(((sender, context) -> MinecraftServer.stopCleanly()));
    }

}