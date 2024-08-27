package net.azisaba.psgui.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class ClickableGUIManager {

    private final List<ClickableGUI> guiList = new ArrayList<>();

    public void registerGUI(ClickableGUI gui) {
        if ( !guiList.contains(gui) ) {
            guiList.add(gui);
        }
    }

    public ClickableGUI getMatchGUI(InventoryView view) {
        for ( ClickableGUI gui : guiList ) {
            if ( gui.isSameInventory(view) ) {
                return gui;
            }
        }

        return null;
    }

    public ClickableGUI getMatchInstance(Class<? extends ClickableGUI> clazz) {
        for ( ClickableGUI gui : guiList ) {
            if ( gui.getClass().equals(clazz) ) {
                return gui;
            }
        }

        return null;
    }

    public void closeAllInventories() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            for ( ClickableGUI gui : guiList ) {
                if ( gui.getTitle().equals(p.getOpenInventory().getTitle()) ) {
                    p.closeInventory();
                    return;
                }
            }
        });
    }
}
