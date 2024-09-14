package cc.davyy.slime.commands;

import cc.davyy.slime.gui.ServerGUI;
import cc.davyy.slime.managers.BossBarManager;
import cc.davyy.slime.managers.LobbyManager;
import cc.davyy.slime.managers.SidebarManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.arguments.minecraft.registry.ArgumentEntityType;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.minestom.server.command.builder.exception.ArgumentSyntaxException;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.FileUtils.getConfig;

// TODO: Remove this command
public class DebugCommand extends Command {

    private final BossBarManager bossBarManager;
    private final SidebarManager sidebarManager;
    private final LobbyManager lobbyManager;

    private final ArgumentEntityType entityTypeArgumentEnum = ArgumentType.EntityType("type");
    private final ArgumentString tagStringArg = ArgumentType.String("tag");

    private final ArgumentWord modeArg = ArgumentType.Word("mode").from("set", "add");

    private final ArgumentNumber<Integer> valueArg = ArgumentType.Integer("value").between(0, 100);

    @Inject
    public DebugCommand(BossBarManager bossBarManager, SidebarManager sidebarManager, LobbyManager lobbyManager) {
        super("debug");
        this.bossBarManager = bossBarManager;
        this.sidebarManager = sidebarManager;
        this.lobbyManager = lobbyManager;

        setArgumentCallback(this::onModeError, modeArg);
        setArgumentCallback(this::onValueError, valueArg);

        addSyntax(this::execute, ArgumentType.Literal("brandname"));
        addSyntax(this::executeInstances, ArgumentType.Literal("instances"));
        addSyntax(this::executeMainInstanceCheck, ArgumentType.Literal("ismaininstance"));
        addSyntax(this::gui, ArgumentType.Literal("servergui"));
        addSyntax(this::checkAnimationStatus, ArgumentType.Literal("animationstatus"));
        addSyntax(this::listAnimationStyles, ArgumentType.Literal("animationstyles"));
        addSyntax(this::removeSidebar, ArgumentType.Literal("rsidebar"));
        addSyntax(this::showBossBar, ArgumentType.Literal("show"));
        addSyntax(this::hideBossBar, ArgumentType.Literal("hide"));
        addSyntax(this::tryDisguise, ArgumentType.Literal("disguise"), entityTypeArgumentEnum);
        addSyntax(this::debugTags, ArgumentType.Literal("debugitem"), tagStringArg);

        addSyntax(this::sendSuggestionMessage, ArgumentType.Literal("healthtest"), modeArg);
        addSyntax(this::onHealthCommand, ArgumentType.Literal("healthtest"), modeArg, valueArg);
    }

    private void onModeError(CommandSender sender, ArgumentSyntaxException exception) {
        sender.sendMessage(Component.text("SYNTAX ERROR: '" + exception.getInput() + "' should be replaced by 'set' or 'add'"));
    }

    private void onValueError(CommandSender sender, ArgumentSyntaxException exception) {
        final int error = exception.getErrorCode();
        final String input = exception.getInput();
        switch (error) {
            case ArgumentNumber.NOT_NUMBER_ERROR:
                sender.sendMessage(Component.text("SYNTAX ERROR: '" + input + "' isn't a number!"));
                break;
            case ArgumentNumber.TOO_LOW_ERROR:
            case ArgumentNumber.TOO_HIGH_ERROR:
                sender.sendMessage(Component.text("SYNTAX ERROR: " + input + " is not between 0 and 100"));
                break;
        }
    }

    private void sendSuggestionMessage(CommandSender sender, CommandContext context) {
        sender.sendMessage(Component.text("/health " + context.get("mode") + " [Integer]"));
    }

    private void onHealthCommand(CommandSender sender, CommandContext context) {
        final Player player = (Player) sender;
        final String mode = context.get("mode");
        final int value = context.get("value");

        switch (mode.toLowerCase()) {
            case "set":
                player.setHealth(value);
                break;
            case "add":
                player.setHealth(player.getHealth() + value);
                break;
        }

        player.sendMessage(Component.text("You have now " + player.getHealth() + " health"));
    }

    private void debugTags(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final ItemStack itemStack = player.getItemInMainHand();
        final String tag = context.get(tagStringArg);

        String tagValue = itemStack.getTag(Tag.String(tag));

        if (tagValue != null) {
            player.sendMessage(Component.text("The item has the tag '" + tag + "' with value: " + tagValue));
        } else {
            player.sendMessage(Component.text("The item does NOT have the tag: " + tag));
        }

    }

    private void tryDisguise(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final EntityType type = context.get(entityTypeArgumentEnum);

        player.switchEntityType(type);

        player.sendMessage("set disguise to " + type.name());
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

    private void showBossBar(@NotNull CommandSender sender, @NotNull CommandContext context) {
        if (!(sender instanceof SlimePlayer player)) {
            sender.sendMessage("Only players can execute this command.");
            return;
        }

        Component title = Component.text("Example Boss Bar").color(TextColor.color(255, 0, 0));
        bossBarManager.createBossBar(player, title, 1.0f, BossBar.Color.RED, BossBar.Overlay.PROGRESS);
    }

    private void hideBossBar(@NotNull CommandSender sender, @NotNull CommandContext context) {
        if (!(sender instanceof SlimePlayer player)) {
            sender.sendMessage("Only players can execute this command.");
            return;
        }

        bossBarManager.removeBossBar(player);
    }

}