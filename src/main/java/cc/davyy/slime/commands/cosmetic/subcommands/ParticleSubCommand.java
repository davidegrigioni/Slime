package cc.davyy.slime.commands.cosmetic.subcommands;

import cc.davyy.slime.managers.cosmetics.ParticleCosmeticManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.registry.ArgumentParticle;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.particle.Particle;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.ColorUtils.of;

@Singleton
public class ParticleSubCommand extends Command {

    private final ParticleCosmeticManager particleCosmeticManager;

    private final ArgumentLiteral createArg = ArgumentType.Literal("create");
    private final ArgumentLiteral applyArg = ArgumentType.Literal("apply");
    private final ArgumentLiteral deleteArg = ArgumentType.Literal("delete");

    private final ArgumentString nameArg = ArgumentType.String("name");
    private final ArgumentInteger idArg = ArgumentType.Integer("id");
    private final ArgumentParticle particleModelArg = ArgumentType.Particle("particlemodel");
    private final ArgumentInteger maxSpeedArg = ArgumentType.Integer("maxSpeed");
    private final ArgumentInteger particleCountArg = ArgumentType.Integer("particlecount");

    @Inject
    public ParticleSubCommand(ParticleCosmeticManager particleCosmeticManager) {
        super("particle");
        this.particleCosmeticManager = particleCosmeticManager;

        addSyntax(this::execute, createArg, nameArg, particleModelArg, maxSpeedArg, particleCountArg);
        addSyntax(this::apply, applyArg, idArg);
        addSyntax(this::delete, deleteArg, idArg);
    }

    private void delete(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final int id = context.get(idArg);

        particleCosmeticManager.removeCosmetic(player, id);
    }

    private void apply(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final int id = context.get(idArg);

        particleCosmeticManager.applyCosmetic(player, id);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final String name = context.get(nameArg);
        final Particle modelParticle = context.get(particleModelArg);
        final int maxSpeed = context.get(maxSpeedArg);
        final int particleCount = context.get(particleCountArg);

        particleCosmeticManager.createCosmetic(of(name)
                .parseLegacy()
                .build(), modelParticle, player.getPosition(), player.getPosition(), maxSpeed, particleCount);

        player.sendMessage("created particle cosmetic");
    }

}