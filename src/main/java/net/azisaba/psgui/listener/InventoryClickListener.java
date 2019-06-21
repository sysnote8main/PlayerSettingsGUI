package net.azisaba.psgui.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import net.azisaba.psgui.PlayerSettingsGUI;
import net.azisaba.psgui.inventory.ClickableGUI;

public class InventoryClickListener implements Listener {

	@EventHandler
	public void clickInventory(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player)) {
			return;
		}

		Inventory openingInv = e.getInventory();
		ClickableGUI gui = PlayerSettingsGUI.getPlugin().getGuiManager().getMatchGUI(openingInv);

		if (gui == null)
			return;

		gui.onClickInventory(e);
	}
}
