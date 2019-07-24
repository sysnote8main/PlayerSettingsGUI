package net.azisaba.psgui.inventory;

import java.util.Arrays;

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

public class MainInventory extends ClickableGUI {

    private ItemStack rankingAno, rankingAnoStatusEnable, rankingAnoStatusDisable,
            entryOnRejoin, entryOnRejoinEnable, entryOnRejoinDisable,
            crates, sound,
            comingsoon;

    @Override
    public Inventory createInventory(Player p) {
        initItems();
        SettingsData data = PlayerSettings.getPlugin().getManager().getSettingsData(p);

        Inventory inv = Bukkit.createInventory(null, getSize(), getTitle());

        inv.setItem(11, rankingAno);
        if ( data.isSet("RankingDisplayer.Anonymous") && data.getBoolean("RankingDisplayer.Anonymous") ) {
            inv.setItem(20, rankingAnoStatusEnable);
        } else {
            inv.setItem(20, rankingAnoStatusDisable);
        }

        inv.setItem(15, entryOnRejoin);
        if ( data.isSet("LeonGunWar.EntryOnRejoin") && data.getBoolean("LeonGunWar.EntryOnRejoin") ) {
            inv.setItem(24, entryOnRejoinEnable);
        } else {
            inv.setItem(24, entryOnRejoinDisable);
        }

        inv.setItem(22, comingsoon);

        inv.setItem(48, crates);
        inv.setItem(50, sound);

        return inv;
    }

    @Override
    public void onClickInventory(InventoryClickEvent e) {

        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        if ( clickedItem == null ) {
            return;
        }

        Inventory inv = null;
        boolean playSound = true;
        if ( Arrays.asList(rankingAno, rankingAnoStatusEnable, rankingAnoStatusDisable).contains(clickedItem) ) {

            processToggleRankingAnonymous(p, e.getClickedInventory());

        } else if ( Arrays.asList(entryOnRejoin, entryOnRejoinDisable, entryOnRejoinEnable).contains(clickedItem) ) {

            processToggleEntryOnRejoin(p, e.getClickedInventory());

        } else if ( clickedItem.equals(crates) ) {
            ClickableGUIManager manager = PlayerSettingsGUI.getPlugin().getGuiManager();
            inv = manager.getMatchInstance(CratesInventory.class).createInventory(p);

        } else if ( clickedItem.equals(sound) ) {
            ClickableGUIManager manager = PlayerSettingsGUI.getPlugin().getGuiManager();
            inv = manager.getMatchInstance(SoundControlInventory.class).createInventory(p);

        } else {
            playSound = false;
        }

        if ( inv != null ) {
            p.openInventory(inv);
        }

        if ( playSound ) {
            p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        }
    }

    private void processToggleRankingAnonymous(Player p, Inventory clicked) {
        SettingsData data = PlayerSettings.getPlugin().getManager().getSettingsData(p);
        boolean now = data.isSet("RankingDisplayer.Anonymous") && data.getBoolean("RankingDisplayer.Anonymous");

        now = !now;

        if ( now ) {
            data.set("RankingDisplayer.Anonymous", now);
            clicked.setItem(20, rankingAnoStatusEnable);
        } else {
            data.set("RankingDisplayer.Anonymous", null);
            clicked.setItem(20, rankingAnoStatusDisable);
        }
    }

    private void processToggleEntryOnRejoin(Player p, Inventory clicked) {
        SettingsData data = PlayerSettings.getPlugin().getManager().getSettingsData(p);
        boolean now = data.isSet("LeonGunWar.EntryOnRejoin") && data.getBoolean("LeonGunWar.EntryOnRejoin");

        now = !now;

        if ( now ) {
            data.set("LeonGunWar.EntryOnRejoin", now);
            clicked.setItem(24, entryOnRejoinEnable);
        } else {
            data.set("LeonGunWar.EntryOnRejoin", null);
            clicked.setItem(24, entryOnRejoinDisable);
        }
    }

    @Override
    public int getSize() {
        return 9 * 6;
    }

    @Override
    public String getTitle() {
        return Chat.f("&cPlayer Settings &e- &aMain");
    }

    private final Material enable = Material.EYE_OF_ENDER, disable = Material.ENDER_PEARL;

    private void initItems() {
        if ( crates == null ) {
            crates = ItemHelper.create(Material.CHEST, Chat.f("&6ガチャログの表示設定"), Chat.f("&7指定した確率より高い当たりの通知を無効化します"),
                    Chat.f("&7これは自分の当たりには反映されません"));
        }
        if ( sound == null ) {
            sound = ItemHelper.create(Material.DIAMOND_SPADE, Chat.f("&6銃の音量設定"), Chat.f("&7銃の音量を調節できます"),
                    Chat.f("&7うるさい場合はこの値を下げてください"));
        }
        if ( comingsoon == null ) {
            comingsoon = ItemHelper.create(Material.ANVIL, Chat.f("&6Coming soon..."));
        }

        if ( rankingAno == null ) {
            rankingAno = ItemHelper.create(Material.SIGN, Chat.f("&cキルランキングを匿名にする"), "",
                    Chat.f("&a有効 &7で &4{0} &7に置き換えられます", "{匿名プレイヤー}"), Chat.f("&c無効 &7でMCIDが表示されます"));
        }
        if ( rankingAnoStatusEnable == null ) {
            rankingAnoStatusEnable = ItemHelper.create(enable, Chat.f("&7現在の設定: &a有効"), "",
                    Chat.f("&7あなたの名前は &4{0} &7で表示されます", "{匿名プレイヤー}"));
        }
        if ( rankingAnoStatusDisable == null ) {
            rankingAnoStatusDisable = ItemHelper.create(disable, Chat.f("&7現在の設定: &c無効"), "",
                    Chat.f("&7あなたのMCIDが表示されます"));
        }

        if ( entryOnRejoin == null ) {
            entryOnRejoin = ItemHelper.create(Material.COMPASS, Chat.f("&c途中参加時にエントリーもする"), "",
                    Chat.f("&a有効 &7で途中参加したときに同時にエントリーします"), Chat.f("&7そのため次の試合からは最初から参加することになります"));
        }
        if ( entryOnRejoinEnable == null ) {
            entryOnRejoinEnable = ItemHelper.create(enable, Chat.f("&7現在の設定: &a有効"), "",
                    Chat.f("&7途中参加時に自動でエントリーされます。"), Chat.f("&c注意: エントリーすると次の試合開始時に自動TPされます。"));
        }
        if ( entryOnRejoinDisable == null ) {
            entryOnRejoinDisable = ItemHelper.create(disable, Chat.f("&7現在の設定: &c無効"), "",
                    Chat.f("&7途中参加してもエントリーされません。"), Chat.f("&7(デフォルト値)"));
        }
    }
}
