package cc.davyy.slime.cosmetics;

import cc.davyy.slime.cosmetics.model.*;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.particle.Particle;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class CosmeticFactory {

    public HatCosmetic createHatCosmetic(int id, @NotNull Component name,
                                         @NotNull ItemStack itemStack) {
        return new HatCosmetic(id, name, itemStack);
    }

    public PetCosmetic createPetCosmetic(int id, @NotNull Component name,
                                         @NotNull EntityType entityType) {
        return new PetCosmetic(id, name, entityType);
    }

    public ParticleCosmetic createParticleCosmetic(int id, @NotNull Component name,
                                                   @NotNull Particle particle,
                                                   @NotNull Pos pos, @NotNull Pos posOffset, int maxSpeed,
                                                   int particleCount) {
        return new ParticleCosmetic(id, name, particle, pos, posOffset, maxSpeed, particleCount);
    }

    public ArmorCosmetic createArmorCosmetic(int id, @NotNull Component name, @NotNull ArmorData armorData) {
        return new ArmorCosmetic(id, name, armorData);
    }

}