package cc.davyy.slime.commands.entities;

import cc.davyy.slime.database.HologramDatabase;
import cc.davyy.slime.database.entities.Hologram;
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

import java.sql.SQLException;

import static cc.davyy.slime.utils.ColorUtils.of;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.minestom.server.MinecraftServer.LOGGER;

@Command(name = "hologram", aliases = "holo")
@Permission("slime.hologram")
@Singleton
public class HologramCommand {

    private final HologramDatabase hologramDatabase;
    private final HologramService hologramService;

    @Inject
    public HologramCommand(HologramDatabase hologramDatabase, HologramService hologramService) {
        this.hologramDatabase = hologramDatabase;
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
        try {
            // Query the database for the hologram
            Hologram hologram = hologramDatabase.getHologram(id);

            // Check if the hologram exists
            if (hologram != null) {
                // Send details of the hologram to the player
                player.sendMessage(Component.text("Hologram ID: " + hologram.getId())
                        .append(Component.newline())
                        .append(Component.text("Text: " + hologram.getText())));
            } else {
                // Inform the player that the hologram does not exist
                player.sendMessage(Component.text("Hologram with ID " + id + " does not exist."));
            }
        } catch (SQLException e) {
            // Log the exception and inform the player of the error
            LOGGER.error("Error querying hologram from database", e);
            player.sendMessage(Component.text("An error occurred while querying the hologram from the database."));
        }
    }

}