package cc.davyy.slime.cosmetics;

import cc.davyy.slime.cosmetics.model.Cosmetic;
import cc.davyy.slime.cosmetics.model.ItemCosmetic;
import cc.davyy.slime.cosmetics.model.MobCosmetic;
import cc.davyy.slime.cosmetics.model.ParticleCosmetic;
import com.google.inject.Singleton;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.particle.Particle;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class CosmeticFactory {

    public ItemCosmetic createItemCosmetic(int id, @NotNull String name, @NotNull ItemStack item, @NotNull Cosmetic.CosmeticType type) {
        return new ItemCosmetic(id, name, item, type);
    }

    public ParticleCosmetic createParticleCosmetic(int id, @NotNull String name, @NotNull Particle particleType) {
        return new ParticleCosmetic(id, name, particleType);
    }

    public MobCosmetic createMobCosmetic(int id, @NotNull String name, @NotNull EntityType entityType) {
        return new MobCosmetic(id, name, entityType);
    }

}