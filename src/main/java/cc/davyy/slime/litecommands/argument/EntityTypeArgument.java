package cc.davyy.slime.litecommands.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class EntityTypeArgument extends ArgumentResolver<CommandSender, EntityType> {

    private static final Map<String, EntityType> ENTITY_TYPE_MAP = new HashMap<>();

    static {
        for (EntityType value : EntityType.values()) {
            ENTITY_TYPE_MAP.put(value.namespace().value().toUpperCase(), value);
        }
    }

    @Override
    protected ParseResult<EntityType> parse(Invocation<CommandSender> invocation, Argument<EntityType> context, String argument) {
        EntityType entityType = ENTITY_TYPE_MAP.get(argument.toUpperCase());

        if (entityType == null) {
            return ParseResult.failure("Invalid EntityType!");
        }

        return ParseResult.success(entityType);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<EntityType> argument, SuggestionContext context) {
        return SuggestionResult.of(ENTITY_TYPE_MAP.keySet());
    }

}