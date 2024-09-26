package cc.davyy.slime.commands;

import cc.davyy.slime.gui.cosmetic.CosmeticGUI;
import cc.davyy.slime.gui.cosmetic.HatGUI;
import cc.davyy.slime.gui.cosmetic.ParticleEffectGUI;
import cc.davyy.slime.gui.cosmetic.PetGUI;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Singleton;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;

@Singleton
public class DebugCommand extends Command {

    public DebugCommand() {
        super("debug");
    }

}