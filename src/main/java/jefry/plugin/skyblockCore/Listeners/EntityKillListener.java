package jefry.plugin.skyblockCore.Listeners;

import jefry.plugin.skyblockCore.SkyblockCore;
import jefry.plugin.skyblockCore.island.PlayerStats;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;
import java.util.Map;

public class EntityKillListener implements Listener {

    private final SkyblockCore plugin;
    private final Map<EntityType, Integer> mobXPRewards = new HashMap<>(); // XP rewards per mob type

    public EntityKillListener(SkyblockCore plugin) {
        this.plugin = plugin;

        // Set XP rewards for killing different mobs
        mobXPRewards.put(EntityType.ZOMBIE, 10);
        mobXPRewards.put(EntityType.SKELETON, 15);
        mobXPRewards.put(EntityType.CREEPER, 20);
        mobXPRewards.put(EntityType.ENDERMAN, 30);
        mobXPRewards.put(EntityType.SPIDER, 10);
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        if (player != null) {
            EntityType entityType = event.getEntity().getType();
            PlayerStats stats = plugin.getPlayerStats(player.getUniqueId());

            // Check if the mob killed is in the mobXPRewards map
            if (mobXPRewards.containsKey(entityType)) {
                int xp = mobXPRewards.get(entityType); // Get XP reward for the mob

                Material weapon = player.getInventory().getItemInMainHand().getType();
                if (weapon == Material.BOW) {
                    stats.addBowmanshipXP(xp); // Add Bowmanship XP
                    player.sendMessage("You gained " + xp + " Bowmanship XP.");
                } else if (weapon == Material.DIAMOND_SWORD || weapon == Material.IRON_SWORD || weapon == Material.STONE_SWORD) {
                    stats.addSwordsmanshipXP(xp); // Add Swordsmanship XP
                    player.sendMessage("You gained " + xp + " Swordsmanship XP.");
                }

                plugin.savePlayerStats(player.getUniqueId()); // Save player stats after gaining XP
            }
        }
    }
}
