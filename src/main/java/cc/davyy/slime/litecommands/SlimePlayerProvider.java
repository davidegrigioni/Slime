package cc.davyy.slime.litecommands;

import cc.davyy.slime.model.SlimePlayer;
import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import net.minestom.server.command.CommandSender;

public class SlimePlayerProvider implements ContextProvider<CommandSender, SlimePlayer> {

    @Override
    public ContextResult<SlimePlayer> provide(Invocation<CommandSender> invocation) {
        CommandSender sender = invocation.sender();

        if (sender instanceof SlimePlayer player) {
            return ContextResult.ok(() -> player);
        }

        return ContextResult.error("error");
    }

}