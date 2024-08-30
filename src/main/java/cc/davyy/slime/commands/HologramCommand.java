package cc.davyy.slime.commands;

import cc.davyy.slime.entities.types.ClickableHologram;
import cc.davyy.slime.entities.types.SimpleHologram;
import cc.davyy.slime.entities.utils.HologramType;
import com.asintoto.minestomacr.annotations.AutoRegister;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public class HologramCommand extends Command {

    public HologramCommand() {
        super("hologram", "holo");

        var hologramTextArg = ArgumentType.Component("hologramText");
        var hologramTypeArg = ArgumentType.Enum("hologramType", HologramType.class);

        addSyntax(this::createHolo, hologramTypeArg, hologramTextArg);
    }

    private void createHolo(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;

        Component hologramText = context.get("hologramText");
        HologramType hologramType = context.get("hologramType");

        Pos playerPos = player.getPosition();
        Pos holoPos = new Pos(playerPos.x(), playerPos.y() + 2, playerPos.z());

        switch (hologramType) {
            case SIMPLE -> new SimpleHologram(hologramText, player.getInstance(), holoPos);
            case CLICKABLE -> new ClickableHologram(hologramText, player.getInstance(), holoPos, () -> player.sendMessage(Component.text("You clicked the hologram!")));
        }

        player.sendMessage(Component.text("Hologram of type " + hologramType.name() + " created at your position: " + holoPos.x() + ", " + holoPos.y() + ", " + holoPos.z()));
    }

}