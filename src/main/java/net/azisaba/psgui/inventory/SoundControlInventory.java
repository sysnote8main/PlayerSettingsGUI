package net.azisaba.psgui.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.azisaba.playersettings.PlayerSettings;
import net.azisaba.playersettings.util.SettingsData;
import net.azisaba.psgui.PlayerSettingsGUI;
import net.azisaba.psgui.utils.Chat;
import net.azisaba.psgui.utils.ItemHelper;

public class SoundControlInventory extends ClickableGUI {

	private final String key = "SoundAbsorber.Percentage";

	private ItemStack increaseLittle, increaseLarge, decreaseLittle, decreaseLarge, backArrow;

	@Override
	public Inventory createInventory(Player p) {
		initItems();

		Inventory inv = Bukkit.createInventory(null, getSize(), getTitle());

		inv.setItem(0, decreaseLarge);
		inv.setItem(1, decreaseLarge);
		inv.setItem(2, decreaseLittle);
		inv.setItem(3, decreaseLittle);
		inv.setItem(4, getMiddleSign(p));
		inv.setItem(5, increaseLittle);
		inv.setItem(6, increaseLittle);
		inv.setItem(7, increaseLarge);
		inv.setItem(8, increaseLarge);

		inv.setItem(22, backArrow);

		return inv;
	}

	@Override
	public void onClickInventory(InventoryClickEvent e) {
		e.setCancelled(true);

		Player p = (Player) e.getWhoClicked();
		ItemStack clicked = e.getCurrentItem();

		if (clicked == null) {
			return;
		}

		if (clicked.equals(backArrow)) {
			p.openInventory(PlayerSettingsGUI.getPlugin().getGuiManager().getMatchInstance(MainInventory.class)
					.createInventory(p));
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			return;
		}

		double add = 0;
		if (clicked.equals(increaseLarge)) {
			add = 5;
		} else if (clicked.equals(increaseLittle)) {
			add = 1;
		} else if (clicked.equals(decreaseLarge)) {
			add = -5;
		} else if (clicked.equals(decreaseLittle)) {
			add = -1;
		} else {
			return;
		}

		SettingsData data = PlayerSettings.getPlugin().getManager().getSettingsData(p);
		double current = 100;

		if (data.isSet(key))
			current = data.getDouble(key);

		current += add;

		if (current > 100) {
			current = 100;
		} else if (current < 0) {
			current = 0;
		}

		data.set(key, current);
		p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);

		e.getClickedInventory().setItem(4, getMiddleSign(p));
	}

	private ItemStack getMiddleSign(Player p) {
		SettingsData data = PlayerSettings.getPlugin().getManager().getSettingsData(p);

		double percent = 100;
		if (data.isSet(key))
			percent = data.getDouble(key);

		ItemStack sign = ItemHelper.create(Material.SIGN, Chat.f("&7銃声の設定"), "", Chat.f("&a現在の音量: &e{0}%", percent),
				"");

		return sign;
	}

	private void initItems() {
		if (increaseLittle == null)
			increaseLittle = ItemHelper.createItem(Material.STAINED_GLASS_PANE, 5, Chat.f("&a音量を {0} 上げる", 1));
		if (increaseLarge == null)
			increaseLarge = ItemHelper.createItem(Material.STAINED_GLASS_PANE, 13, Chat.f("&a音量を {0} 上げる", 5));

		if (decreaseLittle == null)
			decreaseLittle = ItemHelper.createItem(Material.STAINED_GLASS_PANE, 1, Chat.f("&c音量を {0} 下げる", 1));
		if (decreaseLarge == null)
			decreaseLarge = ItemHelper.createItem(Material.STAINED_GLASS_PANE, 14, Chat.f("&c音量を {0} 下げる", 5));

		if (backArrow == null)
			backArrow = ItemHelper.create(Material.ARROW, Chat.f("&6戻る"));
	}

	@Override
	public int getSize() {
		return 9 * 3;
	}

	@Override
	public String getTitle() {
		return Chat.f("&cPlayer Settings &e- &aSound Control");
	}
}
