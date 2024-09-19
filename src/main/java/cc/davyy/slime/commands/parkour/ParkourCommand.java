package cc.davyy.slime.commands.parkour;

import cc.davyy.slime.managers.ParkourManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.jetbrains.annotations.NotNull;

@Singleton
public class ParkourCommand extends Command {

    private final ArgumentString nameArg = ArgumentType.String("parkourname");
    private final ArgumentLiteral createArg = ArgumentType.Literal("create");

    private final ParkourManager parkourManager;

    @Inject
    public ParkourCommand(ParkourManager parkourManager) {
        super("parkour");
        this.parkourManager = parkourManager;

        addSyntax(this::createParkour, createArg, nameArg);
    }

    private void createParkour(@NotNull CommandSender sender, @NotNull CommandContext context) {


    }

}