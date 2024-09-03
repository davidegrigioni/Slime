package cc.davyy.slime.commands;

import com.asintoto.minestomacr.annotations.AutoRegister;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public class ListCommand extends Command {

    private final LuckPerms luckPerms = LuckPermsProvider.get();

    public ListCommand() {
        super("list");

        setDefaultExecutor(this::execute);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        
    }

}