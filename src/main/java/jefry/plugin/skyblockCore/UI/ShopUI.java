package jefry.plugin.skyblockCore.UI;

import jefry.plugin.skyblockCore.SkyblockCore;
import jefry.plugin.skyblockCore.libs.ItemStackPlus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ShopUI implements Listener {

    private final SkyblockCore plugin;

    public ShopUI(SkyblockCore plugin) {
        this.plugin = plugin;
    }

    public void openShopMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Skyblock Shop");

        // Example shop items - prices set in the config
        ItemStackPlus wheatItem = new ItemStackPlus(Material.WHEAT);
        wheatItem.setName("&aBuy Wheat");
        wheatItem.addLore("&7Price: " + plugin.getConfig().getInt("shop.wheat.price") + " coins");

        ItemStackPlus ironItem = new ItemStackPlus(Material.IRON_INGOT);
        ironItem.setName("&aBuy Iron Ingot");
        ironItem.addLore("&7Price: " + plugin.getConfig().getInt("shop.iron.price") + " coins");

        inventory.setItem(10, wheatItem.getItem());
        inventory.setItem(11, ironItem.getItem());

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Skyblock Shop")) return;

        event.setCancelled(true);  // Prevent moving items
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        if (event.getCurrentItem() != null) {
            if (slot == 10) {  // Wheat
                handlePurchase(player, "wheat", Material.WHEAT);
            } else if (slot == 11) {  // Iron Ingot
                handlePurchase(player, "iron", Material.IRON_INGOT);
            }
        }
    }

    private void handlePurchase(Player player, String itemName, Material material) {
        int price = plugin.getConfig().getInt("shop." + itemName + ".price");
        if (plugin.getCoinManager().deductCoins(player.getUniqueId(), price)) {
            player.getInventory().addItem(new org.bukkit.inventory.ItemStack(material, 1));
            player.sendMessage("You bought " + material.name() + " for " + price + " coins.");
        } else {
            player.sendMessage("You don't have enough coins.");
        }
    }
}
