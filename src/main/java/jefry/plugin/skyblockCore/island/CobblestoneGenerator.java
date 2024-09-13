package jefry.plugin.skyblockCore.island;

import jefry.plugin.skyblockCore.SkyblockCore;
import jefry.plugin.skyblockCore.enchantments.CustomEnchantments;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;

import java.util.Random;

public class CobblestoneGenerator implements Listener {

    private final SkyblockCore plugin;
    private final Random random = new Random();

    public CobblestoneGenerator(SkyblockCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // Only process Cobblestone blocks
        if (event.getBlock().getType() == Material.COBBLESTONE) {
            Player player = event.getPlayer();

            // Get the player's generator level (replace with actual player data retrieval)
            int generatorLevel = getGeneratorLevel(player);

            // Get a random ore based on the generator level
            Material randomOre = getRandomOre(generatorLevel);

            // Prevent default cobblestone drop
            event.setDropItems(false);

            // Check if the player has the AutoPickup enchantment
            ItemStack tool = player.getInventory().getItemInMainHand();
            Enchantment autoPickupEnchantment = CustomEnchantments.getEnchantments().get("autopickup");

            // If the tool has the AutoPickup enchantment, add directly to inventory
            if (autoPickupEnchantment != null && tool.containsEnchantment(autoPickupEnchantment)) {
                player.getInventory().addItem(new ItemStack(randomOre));
            } else {
                // Otherwise, drop the item naturally in the world
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(randomOre));
            }
        }
    }

    // Example method to get a player's generator level; replace with your actual logic
    private int getGeneratorLevel(Player player) {
        // Fetch player's generator level from storage (replace with actual code)
        return plugin.getIslandManager().getIsland(player.getUniqueId()).getGeneratorLevel();
    }

    // Method to determine which ore should drop based on generator level
    private Material getRandomOre(int level) {
        int chance = random.nextInt(100);  // Generate a random number between 0 and 99

        // Logic to drop ores based on the generator level
        switch (level) {
            case 1:
                return chance < 80 ? Material.COBBLESTONE : Material.COAL_ORE;
            case 2:
                return chance < 50 ? Material.IRON_ORE : Material.COAL_ORE;
            case 3:
                if (chance < 30) return Material.GOLD_ORE;
                return chance < 60 ? Material.IRON_ORE : Material.COAL_ORE;
            case 4:
                if (chance < 20) return Material.DIAMOND_ORE;
                return chance < 50 ? Material.GOLD_ORE : Material.IRON_ORE;
            case 5:
                if (chance < 10) return Material.EMERALD_ORE;
                return chance < 50 ? Material.DIAMOND_ORE : Material.GOLD_ORE;
            default:
                return Material.COBBLESTONE;  // Default to cobblestone if the level is invalid
        }
    }
}
