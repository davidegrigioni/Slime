package cc.davyy.slime.commands.admin;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.gameplay.BroadcastService;
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

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;

@Command(name = "broadcast")
@Permission("slime.broadcast")
@Singleton
public class BroadCastCommand {

    private final BroadcastService broadcastService;

    @Inject
    public BroadCastCommand(BroadcastService broadcastService) {
        this.broadcastService = broadcastService;
    }

    @Execute
    void usage(@Context CommandSender sender) {
        final Component usageMessage = text("Usage Instructions:")
                .color(NamedTextColor.RED)
                .append(newline())
                .append(text("/broadcast [message]\n").color(NamedTextColor.WHITE))
                .append(text("/broadcast title <title>\n").color(NamedTextColor.WHITE))
                .append(text("/broadcast title <title> <subtitle>").color(NamedTextColor.WHITE));
        sender.sendMessage(usageMessage);
    }

    @Execute
    void executeBroadcast(@Context CommandSender sender, @Arg String message) {
        final String finalMessage = String.join(" ", message);

        broadcastService.broadcastMessage(sender, finalMessage);
    }

    @Execute(name = "title")
    void executeBroadcastTitle(@Context SlimePlayer player, @Arg String title) {
        broadcastService.broadcastTitle(player, title);
    }

    @Execute(name = "title")
    void executeBroadcastTitleSub(@Context SlimePlayer player, @Arg String title, @Arg String subtitle) {
        broadcastService.broadcastTitle(player, title, subtitle);
    }

    @Execute(name = "title duration")
    void executeBroadcastTitleSubTime(@Context SlimePlayer player, @Arg String title, @Arg String subtitle) {
        broadcastService.broadcastTitleWithTimes(player, title, subtitle);
    }

}