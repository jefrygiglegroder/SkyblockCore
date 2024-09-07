package jefry.plugin.skyblockCore.island;

import jefry.plugin.skyblockCore.SkyblockCore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class CobblestoneGenerator implements Listener {

    private final SkyblockCore plugin;
    private final Random random = new Random();

    public CobblestoneGenerator(SkyblockCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.COBBLESTONE) {
            Player player = event.getPlayer();
            int generatorLevel = getGeneratorLevel(player);
            Material newMaterial = getRandomOre(generatorLevel);
            event.setDropItems(false);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(newMaterial));
        }
    }

    private int getGeneratorLevel(Player player) {
        // Fetch player's generator level from storage
        return 1; // Example level, implement actual level fetching
    }

    private Material getRandomOre(int level) {
        // Logic to determine ore type based on generator level
        int chance = random.nextInt(100);
        if (level == 1) {
            return chance < 80 ? Material.COBBLESTONE : Material.COAL_ORE;
        } else if (level == 2) {
            return chance < 50 ? Material.IRON_ORE : Material.COAL_ORE;
        }
        // Continue for more levels
        return Material.COBBLESTONE;
    }
}
