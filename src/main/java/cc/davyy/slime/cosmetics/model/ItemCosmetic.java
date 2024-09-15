package cc.davyy.slime.cosmetics.model;

import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ItemCosmetic(int id, @NotNull String name, @NotNull ItemStack item, @NotNull CosmeticType type)
        implements Cosmetic<ItemStack> {

    public ItemCosmetic {
        if (type != CosmeticType.HAT && type != CosmeticType.ARMOR) {
            throw new IllegalArgumentException("ItemCosmetic must be of type HAT or ARMOR");
        }
    }

    @Override
    public @NotNull ItemStack data() {
        return item;
    }

}