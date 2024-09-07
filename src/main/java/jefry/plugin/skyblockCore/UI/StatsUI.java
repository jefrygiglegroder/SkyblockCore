package jefry.plugin.skyblockCore.UI;

import jefry.plugin.skyblockCore.SkyblockCore;
import jefry.plugin.skyblockCore.island.PlayerStats;
import jefry.plugin.skyblockCore.libs.ItemStackPlus; // Correct import path
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class StatsUI implements Listener {

    private final SkyblockCore plugin;

    public StatsUI(SkyblockCore plugin) {
        this.plugin = plugin;
    }

    public void openStatsMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Player Stats");

        // Create items using ItemStackPlus
        ItemStackPlus farmingItem = new ItemStackPlus(Material.WHEAT);
        farmingItem.setName("&aFarming Level: " + plugin.getPlayerStats(player.getUniqueId()).getFarmingLevel());

        ItemStackPlus miningItem = new ItemStackPlus(Material.IRON_PICKAXE);
        miningItem.setName("&aMining Level: " + plugin.getPlayerStats(player.getUniqueId()).getMiningLevel());

        ItemStackPlus swordItem = new ItemStackPlus(Material.DIAMOND_SWORD);
        swordItem.setName("&aSwordsmanship Level: " + plugin.getPlayerStats(player.getUniqueId()).getSwordsmanshipLevel());

        ItemStackPlus bowItem = new ItemStackPlus(Material.BOW);
        bowItem.setName("&aBowmanship Level: " + plugin.getPlayerStats(player.getUniqueId()).getBowmanshipLevel());

        ItemStackPlus upgradeItem = new ItemStackPlus(Material.EMERALD);
        upgradeItem.setName("&6Click to Upgrade");

        // Set the items into the inventory
        inventory.setItem(10, farmingItem.getItem());
        inventory.setItem(11, miningItem.getItem());
        inventory.setItem(12, swordItem.getItem());
        inventory.setItem(13, bowItem.getItem());
        inventory.setItem(22, upgradeItem.getItem());

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Player Stats")) return;

        event.setCancelled(true); // Prevent moving items in this inventory
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        if (slot == 22) { // Upgrade item slot
            // Handle upgrading logic
            upgradeStats(player);
        }
    }

    private void upgradeStats(Player player) {
        PlayerStats stats = plugin.getPlayerStats(player.getUniqueId());

        // Example of upgrading farming level
        stats.increaseFarmingLevel();
        player.sendMessage("Your Farming Level has been upgraded to " + stats.getFarmingLevel() + "!");

        // Save and refresh UI
        plugin.savePlayerStats(player.getUniqueId());
        openStatsMenu(player); // Refresh the UI
    }
}
