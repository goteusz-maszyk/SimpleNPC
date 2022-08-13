package org.gotitim.simplenpc.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.gotitim.simplenpc.EditMenu;

public class MenuClick implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if(inventory == null) return;
        if(!(inventory.getHolder() instanceof EditMenu)) return;

        EditMenu menu = (EditMenu) inventory;


    }
}
