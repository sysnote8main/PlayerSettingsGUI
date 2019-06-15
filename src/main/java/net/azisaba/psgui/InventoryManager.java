package net.azisaba.psgui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.azisaba.playersettings.PlayerSettings;
import net.azisaba.playersettings.util.SettingsData;
import net.azisaba.psgui.utils.Chat;
import net.azisaba.psgui.utils.ItemHelper;

import lombok.NonNull;

public class InventoryManager {

	private static int size = 9 * 5;
	private static String title = Chat.f("&cPlayer Settings &7- ");

	private static ItemStack increasePercentPaneLittle, decreasePercentPaneLittle, increasePercentPaneLarge,
			decreasePercentPaneLarge;

	static {
		increasePercentPaneLittle = ItemHelper.createItem(Material.STAINED_GLASS_PANE, 5, Chat.f("&e0.1%&a上げる"));
		decreasePercentPaneLittle = ItemHelper.createItem(Material.STAINED_GLASS_PANE, 1, Chat.f("&e0.1%&c下げる"));

		increasePercentPaneLarge = ItemHelper.createItem(Material.STAINED_GLASS_PANE, 13, Chat.f("&e1%&a上げる"));
		decreasePercentPaneLarge = ItemHelper.createItem(Material.STAINED_GLASS_PANE, 14, Chat.f("&e1%&c下げる"));
	}

	public static Inventory getSettingsInventory(Player p) {
		SettingsData data = PlayerSettings.getPlugin().getManager().getSettingsData(p);
		Inventory inv = Bukkit.createInventory(null, size, Chat.f("{0}&e{1}", title, p.getName()));

		double cratesPercentage = data.getDouble("CratesPlus.MuteWinningAnnounce");
		if (!data.isSet("CratesPlus.MuteWinningAnnounce")) {
			cratesPercentage = 20d;
		}

		ItemStack cratesMiddle = ItemHelper.create(Material.CHEST, Chat.f("&7ガチャの当たりを表示する確率"),
				Chat.f("&7指定した確率よりも&c低い&7確率のアイテムのみを表示します"), "",
				Chat.f("&7現在の設定: &e{0}%", cratesPercentage), "");

		if (cratesPercentage <= 0.9) {
			ItemHelper.addLore(cratesMiddle, Chat.f("&e注意&a: &c確率が低すぎるため"));
			ItemHelper.addLore(cratesMiddle, Chat.f("    &c当たりを引いてもログが出ない可能性があります！"));
		}

		inv.setItem(20, cratesMiddle);
		inv.setItem(11, increasePercentPaneLittle);
		inv.setItem(12, increasePercentPaneLarge);
		inv.setItem(28, decreasePercentPaneLarge);
		inv.setItem(29, decreasePercentPaneLittle);

		return inv;
	}

	public static double getAddOrSubtractPercentage(@NonNull ItemStack item) {
		if (item.equals(increasePercentPaneLittle)) {
			return 0.1;
		} else if (item.equals(increasePercentPaneLarge)) {
			return 1d;
		} else if (item.equals(decreasePercentPaneLittle)) {
			return -0.1;
		} else if (item.equals(decreasePercentPaneLarge)) {
			return -1d;
		} else {
			return 0;
		}
	}

	public static boolean isSettingsInventory(@NonNull Inventory inv) {
		return inv.getTitle().startsWith(title) && inv.getSize() == size;
	}
}
