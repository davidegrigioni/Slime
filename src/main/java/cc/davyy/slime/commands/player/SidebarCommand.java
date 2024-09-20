package cc.davyy.slime.commands.player;

import cc.davyy.slime.managers.SidebarManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.jetbrains.annotations.NotNull;

@Singleton
public class SidebarCommand extends Command {

    private final SidebarManager sidebarManager;

    private final ArgumentLiteral toggleArg = ArgumentType.Literal("toggle");

    @Inject
    public SidebarCommand(SidebarManager sidebarManager) {
        super("sidebar", "sb");
        this.sidebarManager = sidebarManager;

        addSyntax(this::toggleSidebar, toggleArg);
    }

    private void toggleSidebar(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        sidebarManager.toggleSidebar(player);
    }

}