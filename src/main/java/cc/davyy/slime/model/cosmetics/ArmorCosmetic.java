package cc.davyy.slime.model.cosmetics;

import cc.davyy.slime.model.SlimePlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ArmorCosmetic(int id, @NotNull Component name, @NotNull ArmorData armorData) implements Cosmetic {

    @Override
    public CosmeticType type() {
        return CosmeticType.ARMOR;
    }

    @Override
    public void apply(@NotNull SlimePlayer player) {
        final PlayerInventory playerInventory = player.getInventory();

        playerInventory.setEquipment(EquipmentSlot.HELMET, armorData.helmet());
        playerInventory.setEquipment(EquipmentSlot.CHESTPLATE, armorData.chestplate());
        playerInventory.setEquipment(EquipmentSlot.LEGGINGS, armorData.leggings());
        playerInventory.setEquipment(EquipmentSlot.BOOTS, armorData.boots());
    }

    @Override
    public void remove(@NotNull SlimePlayer player) {
        final PlayerInventory playerInventory = player.getInventory();

        playerInventory.setEquipment(EquipmentSlot.HELMET, ItemStack.AIR);
        playerInventory.setEquipment(EquipmentSlot.CHESTPLATE, ItemStack.AIR);
        playerInventory.setEquipment(EquipmentSlot.LEGGINGS, ItemStack.AIR);
        playerInventory.setEquipment(EquipmentSlot.BOOTS, ItemStack.AIR);
    }

}