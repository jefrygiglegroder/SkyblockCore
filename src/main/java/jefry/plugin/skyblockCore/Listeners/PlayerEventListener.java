package jefry.plugin.skyblockCore.Listeners;

import jefry.plugin.skyblockCore.SkyblockCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;

public class PlayerEventListener implements Listener {

    private final SkyblockCore plugin;

    public PlayerEventListener(SkyblockCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getIslandManager().loadIsland(player);
        // Load other player-specific data if necessary
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getIslandManager().saveIsland(player.getUniqueId()); // Correct method usage
        // Save other player-specific data if necessary
    }
}
