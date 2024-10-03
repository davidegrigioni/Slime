package cc.davyy.slime.commands.entities;

import cc.davyy.slime.managers.general.SkinManager;
import cc.davyy.slime.managers.entities.npc.NPC;
import cc.davyy.slime.managers.entities.npc.NPCManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerSkin;

import java.util.concurrent.CompletableFuture;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;

@Command(name = "npc")
@Permission("slime.npc")
@Singleton
public class NPCCommand {

    private final SkinManager skinManager;
    private final NPCManager npcManager;

    @Inject
    public NPCCommand(SkinManager skinManager, NPCManager npcManager) {
        this.skinManager = skinManager;
        this.npcManager = npcManager;
    }

    @Execute(name = "setskin")
    void changeNPCSkin(@Context SlimePlayer player, @Arg int id, @Arg String name) {
        final NPC npc = npcManager.getNPC(id);
        final CompletableFuture<PlayerSkin> skinFuture = skinManager.getSkinFromUsernameAsync(name);

        skinFuture.thenAccept(skin -> {
            if (skin != null) {
                npc.setPlayerSkin(skin);
                player.sendMessage("set skin to " + name);
                return;
            }

            player.sendMessage("Failed to retrieve skin for username: " + name);
        }).exceptionally(ex -> {
            player.sendMessage("Error while fetching skin for username " + name + ": " + ex.getMessage());
            return null;
        });
    }

    @Execute(name = "setentity")
    void changeNPCEntity(@Context SlimePlayer player, @Arg int id, @Arg EntityType entityType) {
        final NPC npc = npcManager.getNPC(id);

        npc.switchEntityType(entityType);
        player.sendMessage("set new npc entity type " + entityType.name());
    }

    @Execute(name = "move")
    void moveNPC(@Context SlimePlayer player, @Arg int id) {
        final NPC npc = npcManager.getNPC(id);
        final Pos pos = player.getPosition();

        npc.teleport(pos).thenRun(() -> player.sendMessage("teleported npc to your pos"));
    }

    @Execute(name = "setname")
    void changeNPCName(@Context SlimePlayer player, @Arg int id, @Arg String name) {
        final NPC npc = npcManager.getNPC(id);

        if (npc != null) {
            npc.setName(name);
            player.sendMessage("set new name to " + name);
        }

    }

    @Execute(name = "create")
    void createNPC(@Context SlimePlayer player, @Arg String name, @Arg EntityType entityType) {
        final NPC npc = npcManager.createNPC(entityType, name, null, null);

        npc.setInstance(player.getInstance(), player.getPosition()).thenRun(() -> player.sendMessage("created new npc with " + entityType.name() + " with name " + name));
    }

    @Execute
    void showUsage(@Context CommandSender sender) {
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