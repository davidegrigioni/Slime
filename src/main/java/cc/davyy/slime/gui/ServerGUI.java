package cc.davyy.slime.gui;

import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class ServerGUI extends Inventory {

    public ServerGUI() {
        super(InventoryType.CHEST_3_ROW, "Server GUI");

        addInventoryCondition(((player, i, clickType, inventoryConditionResult) -> inventoryConditionResult.setCancel(true)));

        setItemStack(3, ItemStack.of(Material.COMPASS));
    }

}