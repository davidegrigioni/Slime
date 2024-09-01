package cc.davyy.slime.commands;

import cc.davyy.slime.entities.types.ClickableHologram;
import cc.davyy.slime.entities.types.MultiLineHologram;
import cc.davyy.slime.entities.types.SimpleHologram;
import cc.davyy.slime.utils.Messages;
import cc.davyy.slime.utils.PosUtils;
import com.asintoto.minestomacr.annotations.AutoRegister;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentComponent;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AutoRegister
public class HologramCommand extends Command {

    private final ArgumentComponent hologramTextArg = ArgumentType.Component("hologramText");
    private final ArgumentEnum<HologramType> hologramTypeArg = ArgumentType.Enum("hologramType", HologramType.class);

    public HologramCommand() {
        super("hologram", "holo");

        addSyntax(this::createHolo, hologramTypeArg, hologramTextArg);
    }

    private void createHolo(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;

        final Component hologramText = context.get(hologramTextArg);
        final HologramType hologramType = context.get(hologramTypeArg);

        final Pos playerPos = player.getPosition();
        final Pos holoPos = PosUtils.of(playerPos.x(), playerPos.y() + 2, playerPos.z());

        switch (hologramType) {
            case SIMPLE -> new SimpleHologram(hologramText, player.getInstance(),
                    holoPos);
            case MULTI_LINE -> new MultiLineHologram(List.of(hologramText), player.getInstance(),
                    holoPos, 1.5);
            case CLICKABLE -> new ClickableHologram(hologramText, player.getInstance(),
                    holoPos, (audience) ->
                    player.sendMessage(Component.text("You clicked the hologram!")));
        }

        player.sendMessage(Messages.HOLOGRAM
                .addPlaceholder("hologramtype", hologramType.name())
                .asComponent());
    }

    private enum HologramType { SIMPLE, MULTI_LINE, CLICKABLE }

}