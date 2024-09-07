package jefry.plugin.skyblockCore.island;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Island {
    public static final int MAX_GENERATOR_LEVEL = 5; // Example maximum level
    private final UUID ownerId;
    private final Location location;
    private int generatorLevel = 1; // Default level

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

    public int getGeneratorLevel() {
        return generatorLevel;
    }

    public void setGeneratorLevel(int level) {
        this.generatorLevel = level;
    }

    public void save(File file) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("owner", ownerId.toString());
        config.set("location.world", location.getWorld().getName());
        config.set("location.x", location.getX());
        config.set("location.y", location.getY());
        config.set("location.z", location.getZ());
        config.set("generatorLevel", generatorLevel);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Island load(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        UUID ownerId = UUID.fromString(config.getString("owner"));
        Location location = new Location(
                Bukkit.getWorld(config.getString("location.world")),
                config.getDouble("location.x"),
                config.getDouble("location.y"),
                config.getDouble("location.z")
        );
        Island island = new Island(ownerId, location);
        island.setGeneratorLevel(config.getInt("generatorLevel", 1)); // Default to level 1 if not present
        return island;
    }
}
