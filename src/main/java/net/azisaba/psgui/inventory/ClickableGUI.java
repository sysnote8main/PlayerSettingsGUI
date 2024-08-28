package net.azisaba.psgui.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public abstract class ClickableGUI {

    public abstract int getSize();

    public abstract String getTitle();

    public abstract Inventory createInventory(Player p);

    public abstract void onClickInventory(InventoryClickEvent e);

    public boolean isSameInventory(InventoryView invView) {
        return invView.getTopInventory().getSize() == getSize() && getTitle().equals(invView.getTitle());
    }
}
