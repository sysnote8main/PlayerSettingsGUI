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

    private final String rankingAnonymousKey = "RankingDisplayer.Anonymous";
    private final String entryOnRejoinKey = "LeonGunWar.EntryOnRejoin";
    private final String privateChatPlaySoundKey = "PrivateChatNotify.PlaySound";
    private final String privateChatDisplayTitleKey = "PrivateChatNotify.DisplayTitle";
    private final String showKDOnActionBarKey = "LeonGunWar.ShowKDRatioOnActionBar";

    private ItemStack rankingAno, rankingAnoStatusEnable, rankingAnoStatusDisable,
            entryOnRejoin, entryOnRejoinEnable, entryOnRejoinDisable,
            showKD, showKDEnable, showKDDisable,
            soundOnPrivateChat, soundOnPrivateChatEnable, soundOnPrivateChatDisable,
            titleOnPrivateChat, titleOnPrivateChatEnable, titleOnPrivateChatDisable,
            crates, sound;

    @Override
    public Inventory createInventory(Player p) {
        initItems();
        SettingsData data = PlayerSettings.getInstance().getManager().getSettingsData(p);

        Inventory inv = Bukkit.createInventory(null, getSize(), getTitle());

        inv.setItem(10, rankingAno);
        if ( data.isSet(rankingAnonymousKey) && data.getBoolean(rankingAnonymousKey) ) {
            inv.setItem(19, rankingAnoStatusEnable);
        } else {
            inv.setItem(19, rankingAnoStatusDisable);
        }

        inv.setItem(12, entryOnRejoin);
        if ( data.isSet(entryOnRejoinKey) && data.getBoolean(entryOnRejoinKey) ) {
            inv.setItem(21, entryOnRejoinEnable);
        } else {
            inv.setItem(21, entryOnRejoinDisable);
        }

        inv.setItem(13, showKD);
        if ( data.isSet(showKDOnActionBarKey) && data.getBoolean(showKDOnActionBarKey) ) {
            inv.setItem(22, showKDEnable);
        } else {
            inv.setItem(22, showKDDisable);
        }

        inv.setItem(15, soundOnPrivateChat);
        if ( data.isSet(privateChatPlaySoundKey) && data.getBoolean(privateChatPlaySoundKey) ) {
            inv.setItem(24, soundOnPrivateChatEnable);
        } else {
            inv.setItem(24, soundOnPrivateChatDisable);
        }
        inv.setItem(16, titleOnPrivateChat);
        if ( data.isSet(privateChatDisplayTitleKey) && data.getBoolean(privateChatDisplayTitleKey) ) {
            inv.setItem(25, titleOnPrivateChatEnable);
        } else {
            inv.setItem(25, titleOnPrivateChatDisable);
        }

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
            toggle(p, rankingAnonymousKey, e.getClickedInventory(), 19, rankingAnoStatusEnable, rankingAnoStatusDisable);
        } else if ( Arrays.asList(entryOnRejoin, entryOnRejoinDisable, entryOnRejoinEnable).contains(clickedItem) ) {
            toggle(p, entryOnRejoinKey, e.getClickedInventory(), 21, entryOnRejoinEnable, entryOnRejoinDisable);
        } else if ( Arrays.asList(showKD, showKDEnable, showKDDisable).contains(clickedItem) ) {
            toggle(p, showKDOnActionBarKey, e.getClickedInventory(), 22, showKDEnable, showKDDisable); // TODO
        } else if ( Arrays.asList(soundOnPrivateChat, soundOnPrivateChatDisable, soundOnPrivateChatEnable).contains(clickedItem) ) {
            toggle(p, privateChatPlaySoundKey, e.getClickedInventory(), 24, soundOnPrivateChatEnable, soundOnPrivateChatDisable);
        } else if ( Arrays.asList(titleOnPrivateChat, titleOnPrivateChatDisable, titleOnPrivateChatEnable).contains(clickedItem) ) {
            toggle(p, privateChatDisplayTitleKey, e.getClickedInventory(), 25, titleOnPrivateChatEnable, titleOnPrivateChatDisable);
        } else if ( clickedItem.equals(crates) ) {
            ClickableGUIManager manager = PlayerSettingsGUI.getInstance().getGuiManager();
            inv = manager.getMatchInstance(CratesInventory.class).createInventory(p);

        } else if ( clickedItem.equals(sound) ) {
            ClickableGUIManager manager = PlayerSettingsGUI.getInstance().getGuiManager();
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

    private void toggle(Player p, String key, Inventory clickedInventory, int slot, ItemStack enableItem, ItemStack disableItem) {
        SettingsData data = PlayerSettings.getInstance().getManager().getSettingsData(p);
        boolean now = data.isSet(key) && data.getBoolean(key);
        now = !now;
        if ( now ) {
            data.set(key, now);
            clickedInventory.setItem(slot, enableItem);
        } else {
            data.set(key, null);
            clickedInventory.setItem(slot, disableItem);
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

    private final Material enable = Material.ENDER_EYE, disable = Material.ENDER_PEARL;

    private void initItems() {
        if ( crates == null ) {
            crates = ItemHelper.create(Material.CHEST, Chat.f("&6ガチャログの表示設定"), Chat.f("&7指定した確率より高い当たりの通知を無効化します"),
                    Chat.f("&7これは自分の当たりには反映されません"));
        }
        if ( sound == null ) {
            sound = ItemHelper.create(Material.DIAMOND_SHOVEL, Chat.f("&6銃の音量設定"), Chat.f("&7銃の音量を調節できます"),
                    Chat.f("&7うるさい場合はこの値を下げてください"));
        }

        if ( rankingAno == null ) {
            rankingAno = ItemHelper.create(Material.OAK_SIGN, Chat.f("&cキルランキングを匿名にする"), "",
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
                    Chat.f("&7途中参加時に自動でエントリーされます"), Chat.f("&c注意: エントリーすると次の試合開始時に自動TPされます"));
        }
        if ( entryOnRejoinDisable == null ) {
            entryOnRejoinDisable = ItemHelper.create(disable, Chat.f("&7現在の設定: &c無効"), "",
                    Chat.f("&7途中参加してもエントリーされません"), Chat.f("&7(デフォルト値)"));
        }

        if ( showKD == null ) {
            showKD = ItemHelper.create(Material.NETHER_STAR, Chat.f("&c試合中のアクションバーにKDレートを表示する"), "", Chat.f("&7試合中のアクションバーに、その試合のKDレートが表示されます"), Chat.f("&7バーが長すぎて見えにくくなる可能性があります"));
        }
        if ( showKDEnable == null ) {
            showKDEnable = ItemHelper.create(enable, Chat.f("&7現在の設定: &a有効"), Chat.f("&7試合時のアクションバーにKDレートが表示&aされます"));
        }
        if ( showKDDisable == null ) {
            showKDDisable = ItemHelper.create(disable, Chat.f("&7現在の設定: &c無効"), Chat.f("&7試合時のアクションバーにKDレートは表示&cされません"));
        }

        if ( soundOnPrivateChat == null ) {
            soundOnPrivateChat = ItemHelper.create(Material.ANVIL, Chat.f("&c個人チャット受信時に音を鳴らす"), "",
                    Chat.f("&7個人チャットを受け取ったときに金床の高い音を鳴らします"));
        }
        if ( soundOnPrivateChatEnable == null ) {
            soundOnPrivateChatEnable = ItemHelper.create(enable, Chat.f("&7現在の設定: &a有効"), "",
                    Chat.f("&7個人チャットを受信したら音を鳴らします"));
        }
        if ( soundOnPrivateChatDisable == null ) {
            soundOnPrivateChatDisable = ItemHelper.create(disable, Chat.f("&7現在の設定: &c無効"), "",
                    Chat.f("&7個人チャットを受信しても音を鳴らしません"));
        }

        if ( titleOnPrivateChat == null ) {
            titleOnPrivateChat = ItemHelper.create(Material.OAK_SIGN, Chat.f("&c個人チャット受信時にタイトルを表示する"), "",
                    Chat.f("&7個人チャットを受け取ったときに画面上にメッセージを表示します"));
        }
        if ( titleOnPrivateChatEnable == null ) {
            titleOnPrivateChatEnable = ItemHelper.create(enable, Chat.f("&7現在の設定: &a有効"), "",
                    Chat.f("&7個人チャットを受信したらタイトルを表示します"));
        }
        if ( titleOnPrivateChatDisable == null ) {
            titleOnPrivateChatDisable = ItemHelper.create(disable, Chat.f("&7現在の設定: &c無効"), "",
                    Chat.f("&7個人チャットを受信してもタイトルを表示しません"));
        }
    }
}
