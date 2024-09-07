package cc.davyy.slime.commands;

import com.asintoto.minestomacr.annotations.AutoRegister;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.GeneralUtils.sendColorable;

@AutoRegister
public class GameModeCommand extends Command {

    private final ArgumentEnum<GameMode> gameModeArgumentEnum = ArgumentType.Enum("gamemode", GameMode.class)
            .setFormat(ArgumentEnum.Format.UPPER_CASED);

    public GameModeCommand() {
        super("gamemode");

        /*
        set perm
         */
        setCondition(Conditions::playerOnly);

        addSyntax(this::executeSelf, gameModeArgumentEnum);
    }

    private void executeSelf(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        final GameMode gameMode = context.get(gameModeArgumentEnum);

        player.setGameMode(gameMode);
        sendColorable(player, "Your game mode has been updated to " + gameMode.name());
    }

}