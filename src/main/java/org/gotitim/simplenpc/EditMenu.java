package org.gotitim.simplenpc;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class EditMenu implements InventoryHolder {
    private final Inventory inventory;
    private final NPC npc;
    public static final Material FILL_ITEM = Material.GRAY_STAINED_GLASS_PANE;

    public EditMenu(NPC npc) {
        this.npc = npc;
        inventory = Bukkit.createInventory(this, 27, "NPC Editor (" + npc.id + ")");


    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
