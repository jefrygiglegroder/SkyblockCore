package jefry.plugin.skyblockCore.Listeners;

import jefry.plugin.skyblockCore.SkyblockCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;

import static jefry.plugin.skyblockCore.SkyblockCore.plugin;

public class PlayerJoinListener implements Listener {
    private final SkyblockCore plugin;

    public PlayerJoinListener(SkyblockCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            // Automatically create an island for new players
            plugin.getIslandManager().createIsland(player);
        }
    }
}
