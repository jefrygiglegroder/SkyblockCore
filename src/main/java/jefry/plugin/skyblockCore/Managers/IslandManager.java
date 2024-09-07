package jefry.plugin.skyblockCore.Managers;

import jefry.plugin.skyblockCore.SkyblockCore;
import jefry.plugin.skyblockCore.island.Island;
import jefry.plugin.skyblockCore.island.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IslandManager {

    private final SkyblockCore plugin;
    private final Map<UUID, Island> islands = new HashMap<>();
    private final File savesFolder;

    public IslandManager(SkyblockCore plugin) {
        this.plugin = plugin;
        this.savesFolder = new File(plugin.getDataFolder(), "saves");
        if (!savesFolder.exists()) savesFolder.mkdirs();
    }

    public void createIsland(Player player) {
        UUID playerId = player.getUniqueId();
        Location location = findNextAvailableLocation();
        Island island = new Island(playerId, location);
        islands.put(playerId, island);
        saveIslandData(playerId, island); // Corrected method name and usage
        player.teleport(location);
        player.sendMessage("Island created successfully!");
    }

    public void loadIsland(Player player) {
        UUID playerId = player.getUniqueId();
        Island island = getIsland(playerId);
        if (island == null) {
            createIsland(player);
        } else {
            player.teleport(island.getLocation());
        }
    }

    public Island getIsland(UUID playerId) {
        if (islands.containsKey(playerId)) {
            return islands.get(playerId);
        }

        File playerFolder = new File(savesFolder, playerId.toString());
        File islandFile = new File(playerFolder, "island.yml");
        if (islandFile.exists()) {
            Island island = Island.load(islandFile);
            islands.put(playerId, island);
            return island;
        }

        return null;
    }

    public void upgradeGenerator(Player player) {
        UUID playerId = player.getUniqueId();
        Island island = getIsland(playerId);
        if (island != null) {
            int currentLevel = island.getGeneratorLevel();
            if (currentLevel < Island.MAX_GENERATOR_LEVEL) {
                island.setGeneratorLevel(currentLevel + 1);
                saveIslandData(playerId, island); // Corrected method name and usage
                player.sendMessage("Your cobblestone generator has been upgraded to level " + (currentLevel + 1) + "!");
            } else {
                player.sendMessage("Your generator is already at the maximum level!");
            }
        } else {
            player.sendMessage("You need to create an island first.");
        }
    }

    // Method to save the island data to a file
    public void saveIslandData(UUID playerId, Island island) {
        File playerFolder = new File(savesFolder, playerId.toString());
        if (!playerFolder.exists()) playerFolder.mkdirs();

        File islandFile = new File(playerFolder, "island.yml");
        island.save(islandFile);
    }

    // Method to save a specific island by player ID
    public void saveIsland(UUID playerId) {
        Island island = getIsland(playerId);
        if (island != null) {
            saveIslandData(playerId, island);
        }
    }

    public void savePlayerStats(UUID playerId, PlayerStats stats) {
        File playerFolder = new File(savesFolder, playerId.toString());
        if (!playerFolder.exists()) playerFolder.mkdirs();

        File statsFile = new File(playerFolder, "stats.yml");
        stats.save(statsFile);
    }

    public void saveOtherData(UUID playerId, YamlConfiguration otherData) {
        File playerFolder = new File(savesFolder, playerId.toString());
        if (!playerFolder.exists()) playerFolder.mkdirs();

        File otherFile = new File(playerFolder, "other.yml");
        try {
            otherData.save(otherFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAllIslands() {
        for (Map.Entry<UUID, Island> entry : islands.entrySet()) {
            saveIslandData(entry.getKey(), entry.getValue());
        }
    }

    private Location findNextAvailableLocation() {
        int gridSize = 1000; // 1000 blocks apart
        int index = islands.size();
        int x = (index % 10) * gridSize;
        int z = (index / 10) * gridSize;
        World world = Bukkit.getWorld("world");
        return new Location(world, x, 64, z);
    }
}
