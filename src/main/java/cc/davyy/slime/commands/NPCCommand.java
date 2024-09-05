package cc.davyy.slime.commands;

import cc.davyy.slime.entities.NPC;
import cc.davyy.slime.entities.NPCManager;
import cc.davyy.slime.managers.npc.NameTag;
import cc.davyy.slime.managers.npc.NameTagManager;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.ColorUtils;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class NPCCommand extends Command {

    private final NPCManager npcManager;
    private final NameTagManager nameTagManager;

    private final ArgumentLiteral createArg = ArgumentType.Literal("create");
    private final ArgumentString npcNameArg = ArgumentType.String("npcname");

    public NPCCommand(NPCManager npcManager, NameTagManager nameTagManager) {
        super("npc");
        this.npcManager = npcManager;
        this.nameTagManager = nameTagManager;

        addSyntax(this::createNPC, createArg, npcNameArg);
    }

    private void createNPC(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String create = context.get(npcNameArg);

        NPC npc = npcManager.createNPC(EntityType.PLAYER, create, null, null);
        npc.setInstance(player.getInstance(), player.getPosition());
        NameTag nameTag = nameTagManager.createNameTag(npc);
        nameTag.setText(ColorUtils.of(create)
                .parseLegacy()
                .build());
    }

}