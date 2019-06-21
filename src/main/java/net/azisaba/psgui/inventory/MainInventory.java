package net.azisaba.psgui.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.azisaba.psgui.PlayerSettingsGUI;
import net.azisaba.psgui.utils.Chat;
import net.azisaba.psgui.utils.ItemHelper;

public class MainInventory extends ClickableGUI {

	private ItemStack crates, sound;

	private Inventory inv;

	@Override
	public Inventory createInventory(Player p) {
		if (inv == null) {
			initItems();

			inv = Bukkit.createInventory(null, getSize(), getTitle());

			inv.setItem(48, crates);
			inv.setItem(50, sound);
		}

		return inv;
	}

	@Override
	public void onClickInventory(InventoryClickEvent e) {

		e.setCancelled(true);

		Player p = (Player) e.getWhoClicked();
		ItemStack clickedItem = e.getCurrentItem();

		if (clickedItem == null) {
			return;
		}

		Inventory inv = null;
		if (clickedItem.equals(crates)) {
			ClickableGUIManager manager = PlayerSettingsGUI.getPlugin().getGuiManager();
			inv = manager.getMatchInstance(CratesInventory.class).createInventory(p);

		} else if (clickedItem.equals(sound)) {
			ClickableGUIManager manager = PlayerSettingsGUI.getPlugin().getGuiManager();
			inv = manager.getMatchInstance(SoundControlInventory.class).createInventory(p);

		}

		if (inv == null) {
			return;
		}

		p.openInventory(inv);
		p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
	}

	@Override
	public int getSize() {
		return 9 * 6;
	}

	@Override
	public String getTitle() {
		return Chat.f("&cPlayer Settings &e- &aMain");
	}

	private void initItems() {
		if (crates == null) {
			crates = ItemHelper.create(Material.CHEST, Chat.f("&6ガチャログの表示設定"), Chat.f("&7指定した確率より高い当たりの通知を無効化します"),
					Chat.f("&7これは自分の当たりには反映されません"));
		}

		if (sound == null) {
			sound = ItemHelper.create(Material.DIAMOND_SPADE, Chat.f("&6銃の音量設定"), Chat.f("&7銃の音量を調節できます"),
					Chat.f("&7うるさい場合はこの値を下げてください"));
		}
	}
}
