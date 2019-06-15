package net.azisaba.psgui.listener;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.azisaba.playersettings.PlayerSettings;
import net.azisaba.playersettings.util.SettingsData;
import net.azisaba.psgui.InventoryManager;

public class InventoryClickListener implements Listener {

	@EventHandler
	public void onClickInventory(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player)) {
			return;
		}

		Player p = (Player) e.getWhoClicked();
		ItemStack clickedItem = e.getCurrentItem();
		Inventory clickedInv = e.getClickedInventory();
		Inventory inv = e.getInventory();

		if (clickedInv == null || clickedItem == null) {
			return;
		}

		if (!InventoryManager.isSettingsInventory(inv)) {
			return;
		}

		e.setCancelled(true);

		if (InventoryManager.getAddOrSubtractPercentage(clickedItem) != 0) {
			String key = "CratesPlus.MuteWinningAnnounce";
			SettingsData data = PlayerSettings.getPlugin().getManager().getSettingsData(p);

			double now = data.getDouble(key);
			if (!data.isSet("CratesPlus.MuteWinningAnnounce")) {
				now = 20d;
			}

			now += InventoryManager.getAddOrSubtractPercentage(clickedItem);

			if (now > 100) {
				now = 100;
				p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
			} else if (now < 0) {
				now = 0;
				p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
			} else {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_HAT, 1, 1);
			}

			data.set(key, now);

			p.openInventory(InventoryManager.getSettingsInventory(p));
			return;
		}
	}
}
