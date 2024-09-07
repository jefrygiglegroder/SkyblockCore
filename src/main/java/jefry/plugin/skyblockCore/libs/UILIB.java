package jefry.plugin.skyblockCore.libs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class UILIB {
    Plugin plugin;
    Inventory ui;
    ItemStackPlus blank = new ItemStackPlus(Material.GRAY_STAINED_GLASS_PANE);
    String UIName;
    int UISize;
    InventoryHolder holder = null;

    //H7 Main code
    public UILIB(Plugin plugin, String name, int size) {
        this.plugin = plugin;

        UIName = colorify(name);
        UISize = size-1;

        ui = Bukkit.createInventory(null, UISize+1, UIName);

        blank.setName("&0");
    }

    public UILIB(Plugin plugin, int size) {
        this.plugin = plugin;

        UISize = size-1;

        blank.setName("&0");
    }

    public void setTitle(String name) {
        UIName = name;
        ui = Bukkit.createInventory(holder, UISize+1, UIName);
    }

    public void setTitleAndHolder(String name, InventoryHolder holder) {
        this.UIName = name;
        this.holder = holder;
        ui = Bukkit.createInventory(holder, UISize+1, UIName);
    }

    public static String colorify(String str) {
        return str.replace("&", "§");
    }

    public void open(Player player) {
        player.openInventory(ui);
    }

    public void openSync(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                open(player);
            }
        }.runTask(plugin);
    }

    public Inventory getInventory() {
        return ui;
    }

    //H7 Blanks and stuff
    public void background() {
        fillBlank(0, UISize);
    }

    public void setBlankItem(ItemStack item) {
        blank = new ItemStackPlus(item);
    }
    public void fillBlank(int slot1, int slot2) {
        for (int i = slot1; i <= slot2; i++)
            setBlank(i);
    }

    public void setBlank(int slot) {
        ui.setItem(slot, blank.getItem());
    }

    public void createBackground(ItemStack item) {
        fill(item, 0, UISize);
    }

    public void fill(ItemStack item, int slot1, int slot2) {
        for (int i = slot1; i <= slot2; i++)
            set(item, i);
    }

    public void setMultiple(ItemStack[] items) {
        ui.addItem(items);
    }

    public void set(ItemStack item, int slot) {
        ui.setItem(slot, item);
    }

    //H7 Elements
    public void element(Material mat, String name, int amt, int slot) {
        ItemStackPlus item = new ItemStackPlus(mat, amt);
        item.setName(name);
        ui.setItem(slot, item.getItem());
    }

    public void element(Material mat, String name, int amt, int slot, boolean glow) {
        ItemStackPlus item = new ItemStackPlus(mat, amt);
        item.setName(name);
        if (glow) {
            item.addEnchant(Enchantment.FLAME, 0, true);
            item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        }
        ui.setItem(slot, item.getItem());
    }

    public void element(Material mat, String name, int amt, int slot, boolean glow, String[] lore) {
        ItemStackPlus item = new ItemStackPlus(mat, amt);
        item.setName(name);
        if (glow) {
            item.addEnchant(Enchantment.FLAME, 0, true);
            item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        }

        item.setLore(lore);

        ui.setItem(slot, item.getItem());
    }

    public void customElement(ItemStack item, int slot) {
        ui.setItem(slot, item);
    }

    public static ItemStack createItem(Material mat, String name, int amt) {
        ItemStackPlus item = new ItemStackPlus(mat, amt);
        item.setName(name);
        return item.getItem();
    }

    //H7 Callbacks
    public void registerClickFunction(int slot, ClickCallback callback) {
        Bukkit.getPluginManager().registerEvents(new org.bukkit.event.Listener() {
            @org.bukkit.event.EventHandler
            public void onInventoryClick(InventoryClickEvent event) throws IOException {
                if (event.getClickedInventory() != null && event.getClickedInventory().equals(ui)) {
                    if (event.getRawSlot() == slot) {
                        Player player = (Player) event.getWhoClicked();
                        callback.onClick(player, slot);
                        event.setCancelled(true);
                    }
                }
            }
        }, plugin);
    }

    public interface ClickCallback {
        void onClick(Player player, int slot) throws IOException;
    }

    public void registerCloseCallback(Player player, CloseCallback callback) {
        Bukkit.getPluginManager().registerEvents(new org.bukkit.event.Listener() {
            @org.bukkit.event.EventHandler
            public void onInventoryClose(InventoryCloseEvent event) {
                if (event != null && event.getInventory().equals(ui)) {
                    if (player == event.getPlayer()) {
                        callback.onClose(player);
                    }
                }
            }
        }, plugin);
    }

    public interface CloseCallback {
        void onClose(Player player);
    }
}
