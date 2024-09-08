package jefry.plugin.skyblockCore.Listeners;

import jefry.plugin.skyblockCore.SkyblockCore;
import jefry.plugin.skyblockCore.island.PlayerStats;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BlockBreakListener implements Listener {

    private final SkyblockCore plugin;
    private final Map<Material, Integer> blockXPRewards = new HashMap<>(); // XP rewards per block type

    public BlockBreakListener(SkyblockCore plugin) {
        this.plugin = plugin;
        // Set XP rewards for ores
        blockXPRewards.put(Material.IRON_ORE, 10);
        blockXPRewards.put(Material.GOLD_ORE, 15);
        blockXPRewards.put(Material.DIAMOND_ORE, 20);
        blockXPRewards.put(Material.ANCIENT_DEBRIS, 25);

        // Set XP rewards for crops
        blockXPRewards.put(Material.WHEAT, 5);
        blockXPRewards.put(Material.CARROTS, 5);
        blockXPRewards.put(Material.POTATOES, 5);
        blockXPRewards.put(Material.BEETROOTS, 5);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material blockType = event.getBlock().getType();

        // Check if the block broken is in the blockXPRewards map
        if (blockXPRewards.containsKey(blockType)) {
            int xp = blockXPRewards.get(blockType); // Get XP reward for the block
            PlayerStats stats = plugin.getPlayerStats(player.getUniqueId());

            if (isOre(blockType)) {
                stats.addMiningXP(xp); // Add mining XP for ores
                player.sendMessage("You gained " + xp + " Mining XP.");
            } else if (isCrop(blockType)) {
                stats.addFarmingXP(xp); // Add farming XP for crops
                player.sendMessage("You gained " + xp + " Farming XP.");
            }

            plugin.savePlayerStats(player.getUniqueId()); // Save player stats after gaining XP
        }
    }

    // Helper method to check if a block is an ore
    private boolean isOre(Material blockType) {
        return blockType == Material.IRON_ORE || blockType == Material.GOLD_ORE ||
                blockType == Material.DIAMOND_ORE || blockType == Material.ANCIENT_DEBRIS;
    }

    // Helper method to check if a block is a crop
    private boolean isCrop(Material blockType) {
        return blockType == Material.WHEAT || blockType == Material.CARROTS ||
                blockType == Material.POTATOES || blockType == Material.BEETROOTS;
    }
}
