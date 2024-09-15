package cc.davyy.slime.cosmetics.model;

import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public record MobCosmetic(int id, @NotNull String name, @NotNull EntityType entityType)
        implements Cosmetic<EntityType> {

    @Override
    public @NotNull CosmeticType type() {
        return CosmeticType.MOB;
    }

    @Override
    public @NotNull EntityType data() {
        return entityType;
    }

}