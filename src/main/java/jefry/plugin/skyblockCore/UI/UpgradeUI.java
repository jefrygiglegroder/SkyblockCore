package jefry.plugin.skyblockCore.UI;

import jefry.plugin.skyblockCore.SkyblockCore;
import jefry.plugin.skyblockCore.libs.ItemStackPlus; // Correct import path
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class UpgradeUI implements Listener {
    private final SkyblockCore plugin;
    public UpgradeUI(SkyblockCore plugin) {
        this.plugin = plugin;
    }

    public void openUpgradeMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, "Island Upgrades");

        // Create cobblestone generator upgrade item using ItemStackPlus
        ItemStackPlus cobbleGenUpgrade = new ItemStackPlus(Material.STONE_PICKAXE);
        cobbleGenUpgrade.setName("&6Cobblestone Generator Upgrade");
        cobbleGenUpgrade.addLore(new String[]{
                "&7Upgrade your cobblestone generator",
                "&7to increase its speed and efficiency!"
        });

        inventory.setItem(0, cobbleGenUpgrade.getItem()); // Use getItem() to get the ItemStack

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Island Upgrades")) return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.STONE_PICKAXE) {
            // Handle cobblestone generator upgrade
            plugin.getIslandManager().upgradeGenerator(player);
            player.sendMessage("Cobblestone Generator Upgraded!");
            player.closeInventory();
        }
    }
}
