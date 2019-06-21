package net.azisaba.psgui.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class ClickableGUIManager {

	private List<ClickableGUI> guiList = new ArrayList<>();

	public void registerGUI(ClickableGUI gui) {
		if (!guiList.contains(gui)) {
			guiList.add(gui);
		}
	}

	public ClickableGUI getMatchGUI(Inventory inv) {
		for (ClickableGUI gui : guiList) {
			if (gui.isSameInventory(inv)) {
				return gui;
			}
		}

		return null;
	}

	public ClickableGUI getMatchInstance(Class<? extends ClickableGUI> clazz) {
		for (ClickableGUI gui : guiList) {
			if (gui.getClass().equals(clazz)) {
				return gui;
			}
		}

		return null;
	}

	public void closeAllInventories() {
		Bukkit.getOnlinePlayers().forEach(p -> {

			if (p.getOpenInventory() == null || p.getOpenInventory().getTopInventory() == null)
				return;

			for (ClickableGUI gui : guiList) {
				if (gui.getTitle().equals(p.getOpenInventory().getTopInventory().getTitle())) {
					p.closeInventory();
					return;
				}
			}
		});
	}
}
