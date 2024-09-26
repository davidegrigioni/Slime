package cc.davyy.slime.commands.player;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.SidebarService;
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

    private final SidebarService sidebarService;

    private final ArgumentLiteral toggleArg = ArgumentType.Literal("toggle");

    @Inject
    public SidebarCommand(SidebarService sidebarService) {
        super("sidebar", "sb");
        this.sidebarService = sidebarService;

        addSyntax(this::toggleSidebar, toggleArg);
    }

    private void toggleSidebar(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        sidebarService.toggleSidebar(player);
    }

}