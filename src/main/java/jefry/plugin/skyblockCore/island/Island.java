package jefry.plugin.skyblockCore.island;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Island {
    private UUID ownerId;
    private Location location;
    private int generatorLevel; // Field to store the generator level

    // Constructor for Island, starting with a default generator level of 1
    public Island(UUID ownerId, Location location) {
        this.ownerId = ownerId;
        this.location = location;
        this.generatorLevel = 1; // Default level
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public Location getLocation() {
        return location;
    }

    // Getter for generator level
    public int getGeneratorLevel() {
        return generatorLevel;
    }

    // Setter for generator level
    public void setGeneratorLevel(int generatorLevel) {
        this.generatorLevel = generatorLevel;
    }

    // Save island data to a file
    public void save(File file) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("ownerId", ownerId.toString());
        config.set("location.world", location.getWorld().getName());
        config.set("location.x", location.getX());
        config.set("location.y", location.getY());
        config.set("location.z", location.getZ());
        config.set("generatorLevel", generatorLevel); // Save the generator level

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load island data from a file (including generator level)
    public static Island load(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        UUID ownerId = UUID.fromString(config.getString("ownerId"));
        String worldName = config.getString("location.world");
        double x = config.getDouble("location.x");
        double y = config.getDouble("location.y");
        double z = config.getDouble("location.z");
        int generatorLevel = config.getInt("generatorLevel", 1); // Default to level 1 if not found

        Location location = new Location(org.bukkit.Bukkit.getWorld(worldName), x, y, z);
        Island island = new Island(ownerId, location);
        island.setGeneratorLevel(generatorLevel); // Set the loaded generator level

        return island;
    }
}
