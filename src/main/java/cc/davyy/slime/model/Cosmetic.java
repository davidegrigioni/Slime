package cc.davyy.slime.model;

import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record Cosmetic(int id, @NotNull String name, @NotNull String description, @NotNull ItemStack cosmeticItem, @NotNull CosmeticType type) {

    public enum CosmeticType { ITEM, PARTICLE, MOB }

}