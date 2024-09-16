package cc.davyy.slime.cosmetics.model;

import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record HatCosmetic(int id, @NotNull String name, @NotNull ItemStack hat) implements Cosmetic {

    @Override
    public CosmeticType type() {
        return CosmeticType.HAT;
    }

    @Override
    public void apply(@NotNull SlimePlayer player) {
        player.getInventory().setHelmet(hat);
    }

    @Override
    public void remove(@NotNull SlimePlayer player) {
        player.getInventory().setHelmet(ItemStack.AIR);
    }

}