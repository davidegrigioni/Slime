package cc.davyy.slime.cosmetics.model;

import net.minestom.server.particle.Particle;
import org.jetbrains.annotations.NotNull;

public record ParticleCosmetic(int id, @NotNull String name, @NotNull Particle particle)
        implements Cosmetic<Particle> {

    @Override
    public @NotNull CosmeticType type() {
        return CosmeticType.PARTICLE;
    }

    @Override
    public @NotNull Particle data() {
        return particle;
    }

}