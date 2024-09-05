package cc.davyy.slime.commands;

import cc.davyy.slime.model.SlimePlayer;
import com.asintoto.minestomacr.annotations.AutoRegister;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;

@AutoRegister
public class StopCommand extends Command {

    public StopCommand() {
        super("stop");

        setCondition(((sender, commandString) -> switch (sender) {
            case SlimePlayer player -> player.hasPermission("slime.stop");
            case ConsoleSender ignored -> true;
            default -> false;
        }));

        setDefaultExecutor(((sender, context) -> MinecraftServer.stopCleanly()));
    }

}