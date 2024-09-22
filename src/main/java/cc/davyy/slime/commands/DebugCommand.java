package cc.davyy.slime.commands;

import cc.davyy.slime.gui.cosmetic.CosmeticGUI;
import cc.davyy.slime.gui.cosmetic.HatGUI;
import cc.davyy.slime.gui.cosmetic.ParticleEffectGUI;
import cc.davyy.slime.gui.cosmetic.PetGUI;
import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;

public class DebugCommand extends Command {

    public DebugCommand() {
        super("debug");

        addSyntax(((sender, context) -> {
            new CosmeticGUI().open((SlimePlayer) sender);
        }), ArgumentType.Literal("cosmetic"));

        addSyntax(((sender, context) -> {
            new HatGUI().open((SlimePlayer) sender);
        }), ArgumentType.Literal("hat"));

        addSyntax(((sender, context) -> {
            new ParticleEffectGUI().open((SlimePlayer) sender);
        }), ArgumentType.Literal("particle"));

        addSyntax(((sender, context) -> {
            new PetGUI().open((SlimePlayer) sender);
        }), ArgumentType.Literal("pet"));
    }

}