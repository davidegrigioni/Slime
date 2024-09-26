package cc.davyy.slime.commands.cosmetic;

import cc.davyy.slime.commands.cosmetic.subcommands.ArmorSubCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.HatSubCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.ParticleSubCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.PetSubCommand;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.builder.Command;

import static cc.davyy.slime.utils.GeneralUtils.hasPlayerPermission;

@Singleton
public class CosmeticCommand extends Command {

    @Inject private ParticleSubCommand particleSubCommand;
    @Inject private HatSubCommand hatSubCommand;
    @Inject private PetSubCommand petSubCommand;
    @Inject private ArmorSubCommand armorSubCommand;

    @Inject
    public CosmeticCommand() {
        super("cosmetic");

        setCondition((sender, commandString) -> {
            if (!hasPlayerPermission(sender, "slime.cosmetic")) {
                sender.sendMessage(Messages.NO_PERMS.asComponent());
                return false;
            }
            return true;
        });

        addSubcommand(particleSubCommand);
        addSubcommand(hatSubCommand);
        addSubcommand(petSubCommand);
        addSubcommand(armorSubCommand);
    }

}