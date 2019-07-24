package net.azisaba.psgui.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.azisaba.psgui.PlayerSettingsGUI;
import net.azisaba.psgui.inventory.MainInventory;
import net.azisaba.psgui.utils.Chat;

public class SettingsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if ( !(sender instanceof Player) ) {
            sender.sendMessage(Chat.f("&cこのコマンドはプレイヤーのみ有効です"));
            return true;
        }

        Player p = (Player) sender;

        p.openInventory(
                PlayerSettingsGUI.getPlugin().getGuiManager().getMatchInstance(MainInventory.class).createInventory(p));
        return true;
    }
}
