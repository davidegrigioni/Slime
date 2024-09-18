package cc.davyy.slime.commands.cosmetic;

import cc.davyy.slime.commands.cosmetic.subcommands.ArmorSubCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.HatSubCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.ParticleSubCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.PetSubCommand;
import cc.davyy.slime.cosmetics.managers.ArmorCosmeticManager;
import cc.davyy.slime.cosmetics.managers.HatCosmeticManager;
import cc.davyy.slime.cosmetics.managers.ParticleCosmeticManager;
import cc.davyy.slime.cosmetics.managers.PetCosmeticManager;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.builder.Command;

import static cc.davyy.slime.utils.GeneralUtils.hasPlayerPermission;

@Singleton
public class CosmeticCommand extends Command {

    @Inject
    public CosmeticCommand(PetCosmeticManager petCosmeticManager,
                           ParticleCosmeticManager particleCosmeticManager,
                           ArmorCosmeticManager armorCosmeticManager,
                           HatCosmeticManager hatCosmeticManager) {
        super("cosmetic");

        setCondition((sender, commandString) -> {
            if (!hasPlayerPermission(sender, "slime.cosmetic")) {
                sender.sendMessage(Messages.NO_PERMS.asComponent());
                return false;
            }
            return true;
        });

        addSubcommand(new ParticleSubCommand(particleCosmeticManager));
        addSubcommand(new HatSubCommand(hatCosmeticManager));
        addSubcommand(new PetSubCommand(petCosmeticManager));
        addSubcommand(new ArmorSubCommand(armorCosmeticManager));
    }

}