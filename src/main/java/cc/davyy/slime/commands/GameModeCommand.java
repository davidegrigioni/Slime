package cc.davyy.slime.commands;

import cc.davyy.slime.managers.GameModeManager;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.TextColor.color;

public class GameModeCommand extends Command {

    // remember cooldown
    private final GameModeManager gameModeManager;

    private final ArgumentEnum<GameMode> gameModeArgumentEnum = ArgumentType.Enum("gamemode", GameMode.class)
            .setFormat(ArgumentEnum.Format.UPPER_CASED);
    private final ArgumentEntity playerEntity = ArgumentType.Entity("target").onlyPlayers(true);

    @Inject
    public GameModeCommand(GameModeManager gameModeManager) {
        super("gamemode");
        this.gameModeManager = gameModeManager;

        //setCondition(((sender, commandString) -> hasPlayerPermission(sender, "slime.gamemode")));

        setDefaultExecutor((commandSender, commandContext) -> {
            final String usage = """
                /gamemode <gamemode>\s
                /gamemode <gamemode> <player>""";

            final Component usageMessage = text("Usage Instructions:")
                    .color(color(255, 0, 0))
                    .append(newline())
                    .append(text(usage)
                            .color(color(255, 255, 255)));

            commandSender.sendMessage(usageMessage);
        });

        addSyntax(this::executeSelf, gameModeArgumentEnum);
        addSyntax(this::executeOther, gameModeArgumentEnum, playerEntity);
    }

    private void executeSelf(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        final GameMode gameMode = context.get(gameModeArgumentEnum);

        gameModeManager.setGameMode(player, gameMode);
    }

    private void executeOther(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        final GameMode gameMode = context.get(gameModeArgumentEnum);
        final EntityFinder finder = context.get(playerEntity);
        final Player target = finder.findFirstPlayer(player);

        if (target != null) {
            gameModeManager.setGameMode(player, gameMode, target);
        }
    }

}