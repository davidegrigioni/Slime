package cc.davyy.slime.commands.cosmetic;

import cc.davyy.slime.commands.cosmetic.subcommands.ArmorSubCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.HatSubCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.ParticleSubCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.PetSubCommand;
import cc.davyy.slime.cosmetics.managers.ArmorCosmeticManager;
import cc.davyy.slime.cosmetics.managers.HatCosmeticManager;
import cc.davyy.slime.cosmetics.managers.ParticleCosmeticManager;
import cc.davyy.slime.cosmetics.managers.PetCosmeticManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.builder.Command;

@Singleton
public class CosmeticCommand extends Command {

    @Inject
    public CosmeticCommand(PetCosmeticManager petCosmeticManager,
                           ParticleCosmeticManager particleCosmeticManager,
                           ArmorCosmeticManager armorCosmeticManager,
                           HatCosmeticManager hatCosmeticManager) {
        super("cosmetic");

        addSubcommand(new ParticleSubCommand(particleCosmeticManager));
        addSubcommand(new HatSubCommand(hatCosmeticManager));
        addSubcommand(new PetSubCommand(petCosmeticManager));
        addSubcommand(new ArmorSubCommand(armorCosmeticManager));
    }

}