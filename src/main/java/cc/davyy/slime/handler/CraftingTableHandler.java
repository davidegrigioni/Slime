package cc.davyy.slime.handler;

import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public class CraftingTableHandler implements BlockHandler {

    @Override
    public boolean onInteract(@NotNull BlockHandler.Interaction interaction) {
        interaction.getPlayer().openInventory(new Inventory(InventoryType.CRAFTING, "Crafting Table"));
        return true;
    }

    @Override
    public @NotNull NamespaceID getNamespaceId() {
        return NamespaceID.from("minecraft:craft");
    }

}