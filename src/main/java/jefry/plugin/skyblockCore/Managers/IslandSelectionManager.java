package jefry.plugin.skyblockCore.Managers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IslandSelectionManager {
    private final Map<UUID, Location> pos1Map = new HashMap<>();
    private final Map<UUID, Location> pos2Map = new HashMap<>();

    public void setPos1(Player player, Location location) {
        pos1Map.put(player.getUniqueId(), location);
        player.sendMessage("Position 1 set to: " + locationToString(location));
    }

    public void setPos2(Player player, Location location) {
        pos2Map.put(player.getUniqueId(), location);
        player.sendMessage("Position 2 set to: " + locationToString(location));
    }

    public Location getPos1(Player player) {
        return pos1Map.get(player.getUniqueId());
    }

    public Location getPos2(Player player) {
        return pos2Map.get(player.getUniqueId());
    }

    public boolean isSelectionComplete(Player player) {
        return pos1Map.containsKey(player.getUniqueId()) && pos2Map.containsKey(player.getUniqueId());
    }

    private String locationToString(Location location) {
        return String.format("World: %s, X: %d, Y: %d, Z: %d", location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
