package jefry.plugin.skyblockCore.Managers;

import jefry.plugin.skyblockCore.SkyblockCore;
import jefry.plugin.skyblockCore.island.Island;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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

    // Create a new island for a player
    public void createIsland(Player player) {
        UUID playerId = player.getUniqueId();

        // Ensure we're using the "sky" world
        World skyWorld = Bukkit.getWorld("sky");
        if (skyWorld == null) {
            player.sendMessage("The 'sky' world does not exist. Please create it using /island createworld.");
            return;
        }

        Location location = findNextAvailableLocation(skyWorld);
        Island island = new Island(playerId, location);
        islands.put(playerId, island);
        saveIslandData(playerId, island);
        player.teleport(location);
        player.sendMessage("Your island has been created in the 'sky' world!");
    }

    // Load an existing island for a player
    public void loadIsland(Player player) {
        UUID playerId = player.getUniqueId();
        Island island = getIsland(playerId);
        if (island == null) {
            player.sendMessage("You don't have an island yet. Use /island create to make one.");
        } else {
            player.teleport(island.getLocation());
            player.sendMessage("Welcome back to your island!");
        }
    }

    // Method to save a player's island
    public void saveIsland(UUID playerId) {
        Island island = islands.get(playerId);
        if (island != null) {
            File islandFile = new File(savesFolder, playerId.toString() + "_island.yml");
            island.save(islandFile);  // Assuming the Island class has a 'save' method
            plugin.getLogger().info("Island for player " + playerId + " has been saved.");
        } else {
            plugin.getLogger().warning("No island found for player " + playerId + " to save.");
        }
    }

    // Retrieve an island by player UUID
    public Island getIsland(UUID playerId) {
        if (islands.containsKey(playerId)) {
            return islands.get(playerId);
        }

        // Load island from file if it exists
        File islandFile = new File(savesFolder, playerId.toString() + "_island.yml");
        if (islandFile.exists()) {
            Island island = Island.load(islandFile);
            islands.put(playerId, island); // Add to the map after loading
            return island;
        }

        return null; // No island found
    }

    // Export island within the selected region (pos1 and pos2)
    public void exportIsland(Player player) {
        if (!plugin.getIslandSelectionManager().isSelectionComplete(player)) {
            player.sendMessage("You need to set both positions using /pos1 and /pos2.");
            return;
        }

        Location pos1 = plugin.getIslandSelectionManager().getPos1(player);
        Location pos2 = plugin.getIslandSelectionManager().getPos2(player);

        File exportFile = new File(savesFolder, player.getUniqueId() + "_island_export.yml");

        try {
            // Save blocks in region (to be implemented)
            saveRegionToFile(pos1, pos2, exportFile);
            player.sendMessage("Island exported to file!");
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("Failed to export island.");
        }
    }

    // Import island into the selected region (pos1 and pos2)
    public void importIsland(Player player) {
        if (!plugin.getIslandSelectionManager().isSelectionComplete(player)) {
            player.sendMessage("You need to set both positions using /pos1 and /pos2.");
            return;
        }

        Location pos1 = plugin.getIslandSelectionManager().getPos1(player);
        Location pos2 = plugin.getIslandSelectionManager().getPos2(player);

        File importFile = new File(savesFolder, player.getUniqueId() + "_island_export.yml");

        if (importFile.exists()) {
            // Load the blocks into the selected region (to be implemented)
            loadRegionFromFile(pos1, pos2, importFile);
            player.sendMessage("Island imported successfully!");
        } else {
            player.sendMessage("Island export file not found.");
        }
    }

    // Method to save all loaded islands
    public void saveAllIslands() {
        for (Map.Entry<UUID, Island> entry : islands.entrySet()) {
            UUID playerId = entry.getKey();
            Island island = entry.getValue();
            saveIslandData(playerId, island);
        }
        plugin.getLogger().info("All islands have been saved.");
    }

    // Method to save a single island's data
    public void saveIslandData(UUID playerId, Island island) {
        File islandFile = new File(savesFolder, playerId.toString() + "_island.yml");
        island.save(islandFile);
    }

    // Example methods for saving/loading regions
    private void saveRegionToFile(Location pos1, Location pos2, File file) throws IOException {
        YamlConfiguration config = new YamlConfiguration();

        // Get the bounding box coordinates
        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        int blockIndex = 0;

        // Loop through all blocks within the cuboid
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = pos1.getWorld().getBlockAt(x, y, z);

                    // Save block type and data
                    config.set("blocks." + blockIndex + ".type", block.getType().name());
                    config.set("blocks." + blockIndex + ".x", x);
                    config.set("blocks." + blockIndex + ".y", y);
                    config.set("blocks." + blockIndex + ".z", z);

                    // If block has data (like orientation or other metadata), save that as well
                    config.set("blocks." + blockIndex + ".data", block.getBlockData().getAsString());

                    blockIndex++;
                }
            }
        }

        // Save the configuration to the file
        config.save(file);
    }

    private void loadRegionFromFile(Location pos1, Location pos2, File file) {
        if (!file.exists()) {
            System.out.println("The file does not exist!");
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        int blockIndex = 0;

        // Loop through all saved blocks and apply them back to the world
        while (config.contains("blocks." + blockIndex)) {
            String blockTypeStr = config.getString("blocks." + blockIndex + ".type");
            int x = config.getInt("blocks." + blockIndex + ".x");
            int y = config.getInt("blocks." + blockIndex + ".y");
            int z = config.getInt("blocks." + blockIndex + ".z");
            String blockDataStr = config.getString("blocks." + blockIndex + ".data");

            Material blockType = Material.valueOf(blockTypeStr);
            Block block = pos1.getWorld().getBlockAt(x, y, z);
            block.setType(blockType);

            // Apply block data (like orientation, etc.)
            block.setBlockData(org.bukkit.Bukkit.createBlockData(blockDataStr));

            blockIndex++;
        }

    }

    private Location findNextAvailableLocation(World world) {
        int gridSize = 500; // Distance between islands
        int index = islands.size(); // Total number of islands created so far

        // Calculate the X and Z coordinates based on the grid
        int x = (index % 10) * gridSize;
        int z = (index / 10) * gridSize;

        // Return the new island location in the void world
        return new Location(world, x, 100, z); // Y=100 for default island height
    }
}
