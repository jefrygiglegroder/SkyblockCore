package jefry.plugin.skyblockCore.UI;

import jefry.plugin.skyblockCore.SkyblockCore;
import jefry.plugin.skyblockCore.Managers.IslandManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class IslandSelectionUI implements Listener {

    private static final String ISLAND_SELECTION_TITLE = "Select Your Island";

    // Open the selection UI for the player
    public static void openIslandSelectionUI(Player player, SkyblockCore plugin) {
        // Create an inventory with 9 slots (a single row)
        Inventory islandSelection = Bukkit.createInventory(null, 9, ISLAND_SELECTION_TITLE);

        // Create island options with custom names
        ItemStack island1 = createIslandItem(Material.GRASS_BLOCK, "Starter Island", "The basic starting island.");
        ItemStack island2 = createIslandItem(Material.SAND, "Desert Island", "A desert-themed island.");
        ItemStack island3 = createIslandItem(Material.SNOW_BLOCK, "Ice Island", "A cold, icy island.");

        // Add items to the inventory
        islandSelection.setItem(3, island1); // Slot 3
        islandSelection.setItem(4, island2); // Slot 4
        islandSelection.setItem(5, island3); // Slot 5

        // Open the inventory for the player
        player.openInventory(islandSelection);

        // Register the event listener
        Bukkit.getServer().getPluginManager().registerEvents(new IslandSelectionUI(plugin), plugin);
    }

    // Create a custom item for the island selection UI
    private static ItemStack createIslandItem(Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(description));
        item.setItemMeta(meta);
        return item;
    }

    private final SkyblockCore plugin;

    public IslandSelectionUI(SkyblockCore plugin) {
        this.plugin = plugin;
    }

    // Handle click events inside the selection UI
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        // Check if the clicked inventory is the island selection menu
        if (event.getView().getTitle().equals(ISLAND_SELECTION_TITLE)) {
            event.setCancelled(true); // Prevent taking the item out

            // Get the clicked item
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            String islandType = null;
            if (clickedItem.getType() == Material.GRASS_BLOCK) {
                islandType = "starter";
            } else if (clickedItem.getType() == Material.SAND) {
                islandType = "desert";
            } else if (clickedItem.getType() == Material.SNOW_BLOCK) {
                islandType = "ice";
            }

            if (islandType != null) {
                // Create the selected island from the template
                plugin.getIslandManager().createIslandFromTemplate(player, islandType);
                player.closeInventory(); // Close the inventory after selection
            }
        }
    }
}
