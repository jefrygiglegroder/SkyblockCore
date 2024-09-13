package jefry.plugin.skyblockCore.UI;

import jefry.plugin.skyblockCore.SkyblockCore;
import jefry.plugin.skyblockCore.island.PlayerStats;
import jefry.plugin.skyblockCore.libs.ItemStackPlus;
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

        PlayerStats stats = plugin.getPlayerStats(player.getUniqueId());

        // Create items using ItemStackPlus
        ItemStackPlus farmingItem = new ItemStackPlus(Material.WHEAT);
        farmingItem.setName("&aFarming Level: " + stats.getFarmingLevel());

        ItemStackPlus miningItem = new ItemStackPlus(Material.IRON_PICKAXE);
        miningItem.setName("&aMining Level: " + stats.getMiningLevel());

        ItemStackPlus swordItem = new ItemStackPlus(Material.DIAMOND_SWORD);
        swordItem.setName("&aSwordsmanship Level: " + stats.getSwordsmanshipLevel());

        ItemStackPlus bowItem = new ItemStackPlus(Material.BOW);
        bowItem.setName("&aBowmanship Level: " + stats.getBowmanshipLevel());

        // Emerald upgrade items for each stat
        ItemStackPlus upgradeFarming = new ItemStackPlus(Material.EMERALD);
        upgradeFarming.setName("&6Upgrade Farming - 100 Coins");

        ItemStackPlus upgradeMining = new ItemStackPlus(Material.EMERALD);
        upgradeMining.setName("&6Upgrade Mining - 100 Coins");

        ItemStackPlus upgradeSwordsmanship = new ItemStackPlus(Material.EMERALD);
        upgradeSwordsmanship.setName("&6Upgrade Swordsmanship - 100 Coins");

        ItemStackPlus upgradeBowmanship = new ItemStackPlus(Material.EMERALD);
        upgradeBowmanship.setName("&6Upgrade Bowmanship - 100 Coins");

        // Set the items into the inventory
        inventory.setItem(10, farmingItem.getItem());
        inventory.setItem(11, miningItem.getItem());
        inventory.setItem(12, swordItem.getItem());
        inventory.setItem(13, bowItem.getItem());

        inventory.setItem(19, upgradeFarming.getItem());
        inventory.setItem(20, upgradeMining.getItem());
        inventory.setItem(21, upgradeSwordsmanship.getItem());
        inventory.setItem(22, upgradeBowmanship.getItem());

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Player Stats")) return;

        event.setCancelled(true); // Prevent moving items in this inventory
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        PlayerStats stats = plugin.getPlayerStats(player.getUniqueId());

        // Define the cost of upgrades
        int upgradeCost = 100;

        switch (slot) {
            case 19: // Farming upgrade
                if (plugin.getCoinManager().deductCoins(player.getUniqueId(), upgradeCost)) {
                    stats.increaseFarmingLevel();
                    player.sendMessage("Farming Level upgraded to " + stats.getFarmingLevel());
                } else {
                    player.sendMessage("Not enough coins to upgrade Farming Level.");
                }
                break;
            case 20: // Mining upgrade
                if (plugin.getCoinManager().deductCoins(player.getUniqueId(), upgradeCost)) {
                    stats.increaseMiningLevel();
                    player.sendMessage("Mining Level upgraded to " + stats.getMiningLevel());
                } else {
                    player.sendMessage("Not enough coins to upgrade Mining Level.");
                }
                break;
            case 21: // Swordsmanship upgrade
                if (plugin.getCoinManager().deductCoins(player.getUniqueId(), upgradeCost)) {
                    stats.increaseSwordsmanshipLevel();
                    player.sendMessage("Swordsmanship Level upgraded to " + stats.getSwordsmanshipLevel());
                } else {
                    player.sendMessage("Not enough coins to upgrade Swordsmanship Level.");
                }
                break;
            case 22: // Bowmanship upgrade
                if (plugin.getCoinManager().deductCoins(player.getUniqueId(), upgradeCost)) {
                    stats.increaseBowmanshipLevel();
                    player.sendMessage("Bowmanship Level upgraded to " + stats.getBowmanshipLevel());
                } else {
                    player.sendMessage("Not enough coins to upgrade Bowmanship Level.");
                }
                break;
        }

        // Refresh the UI after upgrade
        openStatsMenu(player);
    }
}
