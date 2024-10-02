package cc.davyy.slime.commands.admin;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.gameplay.GameModeService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.GameMode;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;

@Command(name = "gamemode", aliases = "gm")
@Permission("slime.gamemode")
@Singleton
public class GameModeCommand {

    private final GameModeService gameModeService;

    @Inject
    public GameModeCommand(GameModeService gameModeService) {
        this.gameModeService = gameModeService;
    }

    @Execute
    void showUsage(@Context CommandSender sender) {
        final Component usageMessage = text("Usage Instructions:")
                .color(NamedTextColor.RED)
                .append(newline())
                .append(text("/gamemode <gamemode>\n")
                        .color(NamedTextColor.WHITE))
                .append(text("/gamemode <gamemode> <player>")
                        .color(NamedTextColor.WHITE));

        sender.sendMessage(usageMessage);
    }

    @Execute
    private void executeSelf(@Context SlimePlayer player, @Arg GameMode gameMode) {
        gameModeService.setGameMode(player, gameMode);
    }

    @Execute
    void executeOther(@Context SlimePlayer player, @Arg GameMode gameMode, @Arg SlimePlayer target) {
        gameModeService.setGameMode(player, gameMode, target);
    }

}