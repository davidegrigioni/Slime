package cc.davyy.slime.commands.admin;

import cc.davyy.slime.managers.GameModeManager;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.utils.entity.EntityFinder;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.GeneralUtils.hasPlayerPermission;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;

@Singleton
public class GameModeCommand extends Command {

    private final GameModeManager gameModeManager;

    private final ArgumentEnum<GameMode> gameModeArgumentEnum = ArgumentType.Enum("gamemode", GameMode.class)
            .setFormat(ArgumentEnum.Format.UPPER_CASED);
    private final ArgumentEntity playerEntity = ArgumentType.Entity("target").onlyPlayers(true);

    @Inject
    public GameModeCommand(GameModeManager gameModeManager) {
        super("gamemode");
        this.gameModeManager = gameModeManager;

        setCondition((sender, commandString) -> {
            if (!hasPlayerPermission(sender, "slime.gamemode")) {
                sender.sendMessage(Messages.NO_PERMS.asComponent());
                return false;
            }
            return true;
        });

        setDefaultExecutor(this::showUsage);

        addSyntax(this::executeSelf, gameModeArgumentEnum);
        addSyntax(this::executeOther, gameModeArgumentEnum, playerEntity);
    }

    private void showUsage(@NotNull CommandSender commandSender, @NotNull CommandContext context) {
        final Component usageMessage = text("Usage Instructions:")
                .color(NamedTextColor.RED)
                .append(newline())
                .append(text("/gamemode <gamemode>\n")
                        .color(NamedTextColor.WHITE))
                .append(text("/gamemode <gamemode> <player>")
                        .color(NamedTextColor.WHITE));

        commandSender.sendMessage(usageMessage);
    }

    private void executeSelf(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final GameMode gameMode = context.get(gameModeArgumentEnum);

        gameModeManager.setGameMode(player, gameMode);
    }

    private void executeOther(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final GameMode gameMode = context.get(gameModeArgumentEnum);
        final EntityFinder finder = context.get(playerEntity);
        final SlimePlayer target = (SlimePlayer) finder.findFirstPlayer(player);

        if (target != null) {
            gameModeManager.setGameMode(player, gameMode, target);
            return;
        }

        player.sendMessage(Messages.TARGET_PLAYER_NOT_FOUND.asComponent());
    }

}