package cc.davyy.slime.commands;

import cc.davyy.slime.gui.ServerGUI;
import com.asintoto.minestomacr.annotations.AutoRegister;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

@AutoRegister
public class GUITestCommand extends Command {

    public GUITestCommand() {
        super("gui");

        addSyntax(((sender, context) -> {
            final Player player = (Player) sender;
            new ServerGUI().open(player);
        }), ArgumentType.Literal("server"));
    }

}