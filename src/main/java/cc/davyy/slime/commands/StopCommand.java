package cc.davyy.slime.commands;

import com.asintoto.minestomacr.annotations.AutoRegister;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

@AutoRegister
public class StopCommand extends Command {

    public StopCommand() {
        super("stop");

        setCondition(((sender, commandString) -> {
            if (sender instanceof Player player) {
                return player.hasPermission("slime.stop");
            }
            return true;
        }));

        setDefaultExecutor(((sender, context) -> MinecraftServer.stopCleanly()));
    }

}