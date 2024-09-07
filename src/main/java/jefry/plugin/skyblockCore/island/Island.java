package jefry.plugin.skyblockCore.island;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Island {
    private UUID ownerId;
    private Location location;

    public Island(UUID ownerId, Location location) {
        this.ownerId = ownerId;
        this.location = location;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public Location getLocation() {
        return location;
    }

    // Save island data to a file
    public void save(File file) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("ownerId", ownerId.toString());
        config.set("location.world", location.getWorld().getName());
        config.set("location.x", location.getX());
        config.set("location.y", location.getY());
        config.set("location.z", location.getZ());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load island data from a file (if necessary)
    public static Island load(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        UUID ownerId = UUID.fromString(config.getString("ownerId"));
        String worldName = config.getString("location.world");
        double x = config.getDouble("location.x");
        double y = config.getDouble("location.y");
        double z = config.getDouble("location.z");

        Location location = new Location(org.bukkit.Bukkit.getWorld(worldName), x, y, z);
        return new Island(ownerId, location);
    }
}
