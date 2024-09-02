package cc.davyy.slime.commands;

import com.asintoto.minestomacr.annotations.AutoRegister;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Set;

import static cc.davyy.slime.utils.ColorUtils.print;

@AutoRegister
public class ListCommandsCommand extends Command {

    private final ComponentLogger componentLogger = ComponentLogger.logger(ListCommandsCommand.class);
    private final Set<Command> commands = MinecraftServer.getCommandManager().getCommands();

    public ListCommandsCommand() {
        super("listcommands");

        setDefaultExecutor(this::execute);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        commands.forEach(command -> {
            switch (sender) {
                case ConsoleSender ignored -> print(Component.text(Arrays.toString(command.getNames())));
                case Player player -> player.sendMessage(command.getNames());
                default -> componentLogger.info("Default Robo");
            }
        });
    }

}