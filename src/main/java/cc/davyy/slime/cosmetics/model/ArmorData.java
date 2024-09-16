package cc.davyy.slime.cosmetics.model;

import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ArmorData(@NotNull ItemStack helmet, @NotNull ItemStack chestplate,
                        @NotNull ItemStack leggings, @NotNull ItemStack boots) {}