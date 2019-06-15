package net.azisaba.psgui;

import java.util.Arrays;

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

	private static int size = 9 * 4;
	private static String title = Chat.f("&cPlayer Settings &7- ");

	private static ItemStack cratesMute, cratesMuteEnable, cratesMuteDisable;

	static {
		cratesMute = ItemHelper.create(Material.CHEST, Chat.f("&aガチャログのミュート"),
				Chat.f("&a有効&7で他プレイヤーのガチャログが&c聞こえなく&7なります"),
				Chat.f("&c無効&7で&a聞こえる&7ようになります"));
		cratesMuteEnable = ItemHelper.createItem(Material.STAINED_CLAY, 5, Chat.f("&a有効"),
				Chat.f("&e他プレイヤーのガチャログは&c見えません&e！"));
		cratesMuteDisable = ItemHelper.createItem(Material.STAINED_CLAY, 14, Chat.f("&c無効"),
				Chat.f("&e通常通り他プレイヤーのガチャログも&a見えます&e！"));
	}

	public static Inventory getSettingsInventory(Player p) {
		SettingsData data = PlayerSettings.getPlugin().getManager().getSettingsData(p);
		Inventory inv = Bukkit.createInventory(null, size, Chat.f("{0}&e{1}", title, p.getName()));

		inv.setItem(11, cratesMute);
		if (data.getBoolean("CratesPlus.MuteWinningAnnounce")) {
			inv.setItem(20, cratesMuteEnable);
		} else {
			inv.setItem(20, cratesMuteDisable);
		}

		return inv;
	}

	public static boolean isCratesItem(@NonNull ItemStack item) {
		return Arrays.asList(cratesMute, cratesMuteEnable, cratesMuteDisable).contains(item);
	}

	public static boolean isSettingsInventory(@NonNull Inventory inv) {
		return inv.getTitle().startsWith(title) && inv.getSize() == size;
	}
}
