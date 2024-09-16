package cc.davyy.slime.commands.cosmetic;

import cc.davyy.slime.commands.cosmetic.subcommands.ParticleCosmeticSubCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.PetCosmeticSubCommand;
import cc.davyy.slime.cosmetics.managers.ParticleCosmeticManager;
import cc.davyy.slime.cosmetics.managers.PetCosmeticManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.builder.Command;

@Singleton
public class CosmeticCommand extends Command {

    @Inject
    public CosmeticCommand(PetCosmeticManager petCosmeticManager, ParticleCosmeticManager particleCosmeticManager) {
        super("cosmetic");

        addSubcommand(new PetCosmeticSubCommand(petCosmeticManager));
    }

}