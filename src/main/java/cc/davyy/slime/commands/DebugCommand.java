package cc.davyy.slime.commands;

import cc.davyy.slime.gui.ServerGUI;
import cc.davyy.slime.managers.LobbyManager;
import cc.davyy.slime.managers.SidebarManager;
import com.google.inject.Inject;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.FileUtils.getConfig;

// TODO: Remove this command
public class DebugCommand extends Command {

    private final SidebarManager sidebarManager;
    private final LobbyManager lobbyManager;

    @Inject
    public DebugCommand(SidebarManager sidebarManager, LobbyManager lobbyManager) {
        super("debug");
        this.sidebarManager = sidebarManager;
        this.lobbyManager = lobbyManager;

        addSyntax(this::execute, ArgumentType.Literal("brandname"));
        addSyntax(this::executeInstances, ArgumentType.Literal("instances"));
        addSyntax(this::executeMainInstanceCheck, ArgumentType.Literal("ismaininstance"));
        addSyntax(this::gui, ArgumentType.Literal("servergui"));
        addSyntax(this::checkAnimationStatus, ArgumentType.Literal("animationstatus"));
        addSyntax(this::listAnimationStyles, ArgumentType.Literal("animationstyles"));
        addSyntax(this::removeSidebar, ArgumentType.Literal("rsidebar"));
    }

    private void removeSidebar(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        sidebarManager.toggleSidebar(player);
    }

    private void gui(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        new ServerGUI().open(player);
    }

    private void executeMainInstanceCheck(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        final boolean isMain = lobbyManager.isMainInstance(player.getInstance());
        player.sendMessage(isMain ? "You are currently in the main lobby instance." : "You are currently in a shared lobby instance.");
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        String brandName = MinecraftServer.getBrandName();
        player.sendMessage(of(brandName)
                .parseLegacy()
                .build());
    }

    private void executeInstances(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        final Collection<Integer> lobbyIDs = lobbyManager.getAllLobbiesID();
        player.sendMessage(lobbyIDs.toString());
    }

    private void checkAnimationStatus(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        boolean isAnimating = getConfig().getBoolean("branding.animate");
        player.sendMessage(isAnimating ? "Brand name animation is enabled." : "Brand name animation is disabled.");
    }

    private void listAnimationStyles(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = (Player) sender;
        Collection<String> styles = getConfig().getStringList("branding.animation-styles");
        player.sendMessage(of("Animation Styles: " + String.join(", ", styles)).parseLegacy().build());
    }

}