package net.azisaba.psgui;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.azisaba.psgui.commands.SettingsCommand;
import net.azisaba.psgui.listener.InventoryClickListener;
import net.azisaba.psgui.utils.Chat;

import lombok.Getter;

public class PlayerSettingsGUI extends JavaPlugin {

	@Getter
	private static PlayerSettingsGUI plugin;

	@Override
	public void onEnable() {
		plugin = this;

		Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);

		Bukkit.getPluginCommand("settings").setExecutor(new SettingsCommand());
		Bukkit.getPluginCommand("settings").setPermission(Chat.f("&c権限がありません！"));

		Bukkit.getLogger().info(getName() + " enabled.");
	}

	@Override
	public void onDisable() {
		Bukkit.getLogger().info(getName() + " disabled.");
	}
}
