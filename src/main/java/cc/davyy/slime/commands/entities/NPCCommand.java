package cc.davyy.slime.commands.entities;

import cc.davyy.slime.managers.entities.npc.NPC;
import cc.davyy.slime.managers.entities.npc.NPCManager;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.registry.ArgumentEntityType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.GeneralUtils.hasPlayerPermission;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;

@Singleton
public class NPCCommand extends Command {

    private final NPCManager npcManager;

    private final ArgumentInteger idArg = ArgumentType.Integer("id");
    private final ArgumentEntityType entityTypeArg = ArgumentType.EntityType("entityType");
    private final ArgumentString nameArg = ArgumentType.String("npcName");

    @Inject
    public NPCCommand(NPCManager npcManager) {
        super("npc");
        this.npcManager = npcManager;

        setCondition((sender, commandString) -> {
            if (!hasPlayerPermission(sender, "slime.npc")) {
                sender.sendMessage(Messages.NO_PERMS.asComponent());
                return false;
            }
            return true;
        });

        setDefaultExecutor(this::showUsage);

        addSyntax(this::createNPC, entityTypeArg, nameArg);
        addSyntax(this::changeNPCName, idArg, nameArg);
    }

    private void changeNPCName(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String name = context.get(nameArg);
        final int id = context.get(idArg);
        final NPC npc = npcManager.getNPC(id);

        if (npc != null) {
            npc.setName(name);
            player.sendMessage("set new name to " + name);
        }

    }

    private void createNPC(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final EntityType entityType = context.get(entityTypeArg);
        final String name = context.get(nameArg);
        final NPC npc = npcManager.createNPC(entityType, name, null, null);

        npc.setInstance(player.getInstance(), player.getPosition());
        player.sendMessage("created new npc with " + entityType.name() + " with name " + name);
    }

    private void showUsage(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Component usageMessage = text("Usage Instructions:")
                .color(NamedTextColor.RED)
                .append(newline())
                .append(text("/npc create [name]")
                        .color(NamedTextColor.WHITE)
                        .append(text(" - Creates a new NPC with an optional name.")))
                .append(newline())
                .append(text("/npc move <id>")
                        .color(NamedTextColor.WHITE)
                        .append(text(" - Moves the NPC with the specified ID to your current location.")))
                .append(newline())
                .append(text("/npc delete <id>")
                        .color(NamedTextColor.WHITE)
                        .append(text(" - Deletes the NPC with the specified ID.")))
                .append(newline())
                .append(text("Example usage: /npc create MyNPC")
                        .color(NamedTextColor.GREEN)
                        .decorate(TextDecoration.ITALIC));

        sender.sendMessage(usageMessage);
    }

}