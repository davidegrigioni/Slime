package cc.davyy.slime.litecommands.argument;

import cc.davyy.slime.model.SlimePlayer;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SlimePlayerArgument extends ArgumentResolver<CommandSender, SlimePlayer> {

    private final Collection<Player> playerCollection = MinecraftServer.getConnectionManager().getOnlinePlayers();

    @Override
    protected ParseResult<SlimePlayer> parse(Invocation<CommandSender> invocation, Argument<SlimePlayer> context, String argument) {
        CommandSender sender = invocation.sender();

        if (sender instanceof SlimePlayer player) {
            return ParseResult.success(player);
        }

        return ParseResult.failure("Error");
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<SlimePlayer> argument, SuggestionContext context) {
        List<String> playerNames = playerCollection.stream()
                .map(Player::getUsername)
                .collect(Collectors.toList());

        return SuggestionResult.of(playerNames);
    }

}