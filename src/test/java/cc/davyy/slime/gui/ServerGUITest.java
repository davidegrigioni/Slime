package cc.davyy.slime.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerGUITest {

    private static final int NAVIGATOR_SLOT = 3;
    private static final int SETTINGS_SLOT = 5;
    private static final int PLAYER_MANAGEMENT_SLOT = 11;

    static {
        MinecraftServer.init();
    }

    @Test
    void inventoryCreation() {
        Inventory inventory = new Inventory(InventoryType.CHEST_3_ROW, "Server GUI");

        assertEquals(InventoryType.CHEST_3_ROW, inventory.getInventoryType());
        assertEquals(Component.text("Server GUI"), inventory.getTitle());
    }

    @Test
    void inventorySetup() {
        var compass = ItemStack.of(Material.COMPASS);

        Inventory inventory = new Inventory(InventoryType.CHEST_3_ROW, "test");
        assertSame(ItemStack.AIR, inventory.getItemStack(0));
        inventory.setItemStack(NAVIGATOR_SLOT, compass);
        assertSame(compass, inventory.getItemStack(NAVIGATOR_SLOT));
    }

}