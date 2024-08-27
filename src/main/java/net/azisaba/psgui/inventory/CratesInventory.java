package net.azisaba.psgui.inventory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class CratesInventory extends ClickableGUI {

    private final String key = "CratesPlus.MuteWinningAnnounce";
    private final String ownNotifyKey = "CratesPlus.MuteOwnWinningAnnounce";
    private ItemStack addLittle, addLarge, subtractLittle, subtractLarge, backArrow;
    private ItemStack muteOwnEnable, muteOwnDisable;
    private boolean initialized;

    @Override
    public Inventory createInventory(Player p) {
        if(!initialized) init();
        Inventory inv = Bukkit.createInventory(null, getSize(), getTitle());

        inv.setItem(0, subtractLarge);
        inv.setItem(1, subtractLarge);
        inv.setItem(2, subtractLittle);
        inv.setItem(3, subtractLittle);
        inv.setItem(4, getMiddleSign(p));
        inv.setItem(5, addLittle);
        inv.setItem(6, addLittle);
        inv.setItem(7, addLarge);
        inv.setItem(8, addLarge);

        inv.setItem(22, backArrow);

        SettingsData data = PlayerSettings.getInstance().getManager().getSettingsData(p);
        boolean muteOwn = data.isSet(ownNotifyKey) && data.getBoolean(ownNotifyKey);
        if ( muteOwn ) {
            inv.setItem(26, muteOwnEnable);
        } else {
            inv.setItem(26, muteOwnDisable);
        }

        return inv;
    }

    @Override
    public void onClickInventory(InventoryClickEvent e) {
        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        if ( item == null ) {
            return;
        }

        boolean processed = true;
        if ( item.equals(backArrow) ) {
            p.openInventory(PlayerSettingsGUI.getInstance().getGuiManager().getMatchInstance(MainInventory.class).createInventory(p));
        } else if ( item.equals(muteOwnDisable) ) {
            SettingsData data = PlayerSettings.getInstance().getManager().getSettingsData(p);
            data.set(ownNotifyKey, true);
            e.getClickedInventory().setItem(26, muteOwnEnable);
        } else if ( item.equals(muteOwnEnable) ) {
            SettingsData data = PlayerSettings.getInstance().getManager().getSettingsData(p);
            data.set(ownNotifyKey, false);
            e.getClickedInventory().setItem(26, muteOwnDisable);
        } else {
            processed = false;
        }

        if ( processed ) {
            p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }

        double change = 0;

        if ( item.equals(subtractLarge) ) {
            change = -1;
        } else if ( item.equals(subtractLittle) ) {
            change = -0.1;
        } else if ( item.equals(addLittle) ) {
            change = 0.1;
        } else if ( item.equals(addLarge) ) {
            change = 1;
        } else {
            return;
        }

        SettingsData data = PlayerSettings.getInstance().getManager().getSettingsData(p);
        double value = 1d;
        if ( data.isSet(key) ) {
            value = data.getDouble(key);
        }

        value += change;

        if ( value > 100 ) {
            value = 100;
        } else if ( value < 0 ) {
            value = 0;
        }

        // 小数点第2位で四捨五入
        value = new BigDecimal(value).setScale(1, RoundingMode.HALF_UP).doubleValue();

        data.set(key, value);
        p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);

        updateInventory(e.getClickedInventory(), p);
    }

    private void init() {
        initialized = true;
        addLittle = ItemHelper.createItem(Material.GREEN_STAINED_GLASS_PANE, 5, Chat.f("&e0.1%&a上げる"));
        addLarge = ItemHelper.createItem(Material.LIME_STAINED_GLASS_PANE, 13, Chat.f("&e1%&a上げる"));
        subtractLittle = ItemHelper.createItem(Material.ORANGE_STAINED_GLASS_PANE, 1, Chat.f("&e0.1%&c下げる"));
        subtractLarge = ItemHelper.createItem(Material.RED_STAINED_GLASS_PANE, 14, Chat.f("&e1%&c下げる"));
        backArrow = ItemHelper.create(Material.ARROW, Chat.f("&6戻る"));
        muteOwnEnable = ItemHelper.create(Material.ENDER_EYE, Chat.f("&e自分のログにも適用する &7(&a有効&7)"), Chat.f("&7自分のガチャログも&c上の設定が適用&7されます！"));
        muteOwnDisable = ItemHelper.create(Material.ENDER_PEARL, Chat.f("&e自分のログにも適用する &7(&c無効&7)"), Chat.f("&7自分のガチャログは確率を問わず&c全て&7チャットに流れます！"));
    }

    private ItemStack getMiddleSign(Player p) {
        SettingsData data = PlayerSettings.getInstance().getManager().getSettingsData(p);

        double percentage = 1d;
        if ( data.isSet(key) ) {
            percentage = data.getDouble(key);
        }

        // 小数点第2位で四捨五入
        percentage = new BigDecimal(percentage).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        List<String> lore = new ArrayList<>(Arrays.asList(Chat.f("&7これより大きい確率に設定されている当たりは聞こえなくなります。"),
                "", Chat.f("&a現在の設定: &e{0}%", percentage), ""));

        if ( percentage < 1 ) {
            lore.add(4, Chat.f("&c警告: 現在の設定では銃などのレアアイテムの当たりが聞こえない場合があります！"));
            lore.add(5, "");
        }

        return ItemHelper.create(Material.OAK_SIGN, Chat.f("&7表示しない当たりの確率"), lore.toArray(new String[lore.size()]));
    }

    private void updateInventory(Inventory inv, Player p) {
        ItemStack middleSign = inv.getItem(4);
        if ( middleSign == null || middleSign.getType() != Material.OAK_SIGN ) {
            p.openInventory(createInventory(p));
            return;
        }

        middleSign = getMiddleSign(p);

        inv.setItem(4, middleSign);
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }

    @Override
    public String getTitle() {
        return Chat.f("&cPlayer Settings &e- &aCrates");
    }
}
