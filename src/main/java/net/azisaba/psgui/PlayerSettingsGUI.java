package net.azisaba.psgui;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.azisaba.psgui.commands.SettingsCommand;
import net.azisaba.psgui.inventory.ClickableGUIManager;
import net.azisaba.psgui.inventory.CratesInventory;
import net.azisaba.psgui.inventory.MainInventory;
import net.azisaba.psgui.inventory.SoundControlInventory;
import net.azisaba.psgui.listener.InventoryClickListener;
import net.azisaba.psgui.utils.Chat;

import lombok.Getter;

public class PlayerSettingsGUI extends JavaPlugin {

    @Getter
    private static PlayerSettingsGUI plugin;
    @Getter
    private ClickableGUIManager guiManager;

    @Override
    public void onEnable() {
        plugin = this;

        guiManager = new ClickableGUIManager();

        guiManager.registerGUI(new MainInventory());
        guiManager.registerGUI(new CratesInventory());
        guiManager.registerGUI(new SoundControlInventory());

        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);

        Bukkit.getPluginCommand("settings").setExecutor(new SettingsCommand());
        Bukkit.getPluginCommand("settings").setPermissionMessage(Chat.f("&c権限がありません！"));

        Bukkit.getLogger().info(getName() + " enabled.");
    }

    @Override
    public void onDisable() {

        guiManager.closeAllInventories();

        Bukkit.getLogger().info(getName() + " disabled.");
    }
}
