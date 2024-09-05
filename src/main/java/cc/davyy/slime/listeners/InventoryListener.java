package cc.davyy.slime.listeners;

import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryPreClickEvent;

public class InventoryListener {

    public void init(GlobalEventHandler handler) {
        handler.addListener(InventoryPreClickEvent.class, event -> event.setCancelled(true));
    }

}