package cc.davyy.slime.litecommands.argument;

import cc.davyy.slime.litecommands.LocationAxis;
import cc.davyy.slime.model.SlimePlayer;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.MultipleArgumentResolver;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.minestom.server.command.CommandSender;
import net.minestom.server.coordinate.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PosArgument implements MultipleArgumentResolver<CommandSender, Pos> {

    private final static String CURRENT_LOCATION = "~";
    public static final String COORDINATE_FORMAT = "%.2f";

    public Range getRange(Argument<Pos> argument) {
        return Range.of(LocationAxis.SIZE);
    }

    public ParseResult<Pos> parse(Invocation<CommandSender> invocation, Argument<Pos> argument, RawInput rawInput) {
        String input = String.join(RawCommand.COMMAND_SEPARATOR, rawInput.seeNext(LocationAxis.SIZE));

        try {
            double x = parseAxis(invocation, rawInput.next(), LocationAxis.X);
            double y = parseAxis(invocation, rawInput.next(), LocationAxis.Y);
            double z = parseAxis(invocation, rawInput.next(), LocationAxis.Z);

            return ParseResult.success(new Pos(x, y, z));
        } catch (NumberFormatException exception) {
            return ParseResult.failure("Invalid Location");
        }
    }

    private double parseAxis(Invocation<CommandSender> invocation, String input, LocationAxis axis) {
        try {
            return Double.parseDouble(input.replace(",", "."));
        } catch (NumberFormatException exception) {
            if (!input.equals(CURRENT_LOCATION)) {
                throw exception;
            }

            if (!(invocation.sender() instanceof SlimePlayer player)) {
                throw exception;
            }

            Pos position = player.getPosition();

            return axis.getValue(position);
        }
    }

    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Pos> argument, SuggestionContext context) {
        Suggestion current = context.getCurrent();

        if (!isParsable(current)) {
            return SuggestionResult.empty();
        }

        CommandSender sender = invocation.sender();
        String firstPart = current.multilevel();
        SuggestionResult result = SuggestionResult.empty();

        if (!(sender instanceof SlimePlayer player)) {
            return SuggestionResult.of(firstPart);
        }

        Pos position = player.getPosition();

        List<String> currentSuggestion = suggestionsWithoutLast(current);
        List<String> dynamicSuggestion = suggestionsWithoutLast(current);

        if (currentSuggestion.size() == LocationAxis.SIZE) {
            currentSuggestion.remove(LocationAxis.SIZE - 1);
            dynamicSuggestion.remove(LocationAxis.SIZE - 1);
        }

        for (int axisIndex = currentSuggestion.size(); axisIndex < LocationAxis.SIZE; axisIndex++) {
            LocationAxis axis = LocationAxis.at(axisIndex);

            currentSuggestion.add(CURRENT_LOCATION);
            dynamicSuggestion.add(String.format(Locale.US, COORDINATE_FORMAT, axis.getValue(position)));

            result.add(Suggestion.from(currentSuggestion));
            result.add(Suggestion.from(dynamicSuggestion));
        }

        return result;
    }

    private boolean isParsable(Suggestion current) {
        List<String> arguments = suggestionsWithoutLast(current);

        for (String arg : arguments) {
            if (arg.equals(CURRENT_LOCATION)) {
                continue;
            }

            try {
                Double.parseDouble(arg);
            } catch (NumberFormatException exception) {
                return false;
            }
        }

        return true;
    }

    private List<String> suggestionsWithoutLast(Suggestion suggestion) {
        List<String> suggestionList = new ArrayList<>(suggestion.multilevelList());

        if (!suggestion.lastLevel().isEmpty()) {
            return suggestionList;
        }

        suggestionList.removeLast();
        return suggestionList;
    }

}