package net.azisaba.psgui.listener;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import net.azisaba.psgui.PlayerSettingsGUI;
import net.azisaba.psgui.inventory.ClickableGUI;

public class InventoryClickListener implements Listener {

    private final HashMap<UUID, Long> doubleClickPreventer = new HashMap<>();

    @EventHandler
    public void clickInventory(InventoryClickEvent e) {
        if ( !(e.getWhoClicked() instanceof Player) ) {
            return;
        }

        Player p = (Player) e.getWhoClicked();
        ClickableGUI gui = PlayerSettingsGUI.getInstance().getGuiManager().getMatchGUI(p.getOpenInventory());

        if ( gui == null ) {
            return;
        }

        if ( doubleClickPreventer.getOrDefault(p.getUniqueId(), 0L) + 100 > System.currentTimeMillis() ) {
            e.setCancelled(true);
            return;
        }

        gui.onClickInventory(e);

        doubleClickPreventer.put(p.getUniqueId(), System.currentTimeMillis());
    }
}
