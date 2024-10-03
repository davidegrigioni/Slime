package cc.davyy.slime.commands.entities;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.entities.HologramService;
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

import static cc.davyy.slime.utils.ColorUtils.of;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;

@Command(name = "hologram", aliases = "holo")
@Permission("slime.hologram")
@Singleton
public class HologramCommand {

    private final HologramService hologramService;

    @Inject
    public HologramCommand(HologramService hologramService) {
        this.hologramService = hologramService;
    }

    @Execute
    void usage(@Context CommandSender sender) {
        final Component usageMessage = text("Usage Instructions:")
                .color(NamedTextColor.RED)
                .append(newline())
                .append(text("/hologram create <text>\n").color(NamedTextColor.WHITE))
                .append(text("/hologram move <id>\n").color(NamedTextColor.WHITE))
                .append(text("/hologram delete <id>\n").color(NamedTextColor.WHITE))
                .append(text("/hologram addline <id> <text>\n").color(NamedTextColor.WHITE))
                .append(text("/hologram insertline <id> <index> <text>\n").color(NamedTextColor.WHITE))
                .append(text("/hologram removeline <id> <index>\n").color(NamedTextColor.WHITE))
                .append(text("/hologram updateline <id> <index> <text>").color(NamedTextColor.WHITE));

        sender.sendMessage(usageMessage);
    }

    @Execute(name = "create")
    void handleCreate(@Context SlimePlayer player, @Arg String text) {
        hologramService.createHologram(player, of(text).parseLegacy().build());
    }

    @Execute(name = "move")
    void handleMove(@Context SlimePlayer player, @Arg int id) {
        hologramService.moveHologram(id, player);
    }

    @Execute(name = "delete")
    void handleDelete(@Context SlimePlayer player, @Arg int id) {
        hologramService.deleteHologram(id, player);
    }

    @Execute(name = "addline", aliases = "al")
    void handleAddLine(@Context SlimePlayer player, @Arg int id, @Arg String text) {
        hologramService.addHologramLine(player, id, of(text).parseLegacy().build());
    }

    @Execute(name = "insertline", aliases = "il")
    void handleInsertLine(@Context SlimePlayer player, @Arg int id, @Arg int index, @Arg String text) {
        hologramService.insertHologramLine(player, id, index, of(text).parseLegacy().build());
    }

    @Execute(name = "removeline", aliases = "rl")
    void handleRemoveLine(@Context SlimePlayer player, @Arg int id, @Arg int index) {
        hologramService.removeHologramLine(player, id, index);
    }

    @Execute(name = "updateline", aliases = "ul")
    void handleUpdateLine(@Context SlimePlayer player, @Arg int id, @Arg int index, @Arg String newText) {
        hologramService.updateHologramLine(player, id, index, of(newText).parseLegacy().build());
    }

    @Execute(name = "debug", aliases = "db")
    void handleDebug(@Context SlimePlayer player, @Arg int id) {
        hologramService.debug(player, id);
    }

}