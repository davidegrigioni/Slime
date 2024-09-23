package cc.davyy.slime.listeners;

import cc.davyy.slime.gui.LobbyGUI;
import cc.davyy.slime.gui.ServerGUI;
import com.google.inject.Singleton;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

@Singleton
public class InventoryPreClickListener implements EventListener<InventoryPreClickEvent> {

    @Override
    public @NotNull Class<InventoryPreClickEvent> eventType() {
        return InventoryPreClickEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull InventoryPreClickEvent event) {
        final Inventory inventory = event.getInventory();

        if (inventory instanceof ServerGUI || inventory instanceof LobbyGUI) return Result.INVALID;

        event.setCancelled(true);

        return Result.SUCCESS;
    }
}