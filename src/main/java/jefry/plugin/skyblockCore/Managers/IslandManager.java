package jefry.plugin.skyblockCore.Managers;

import jefry.plugin.skyblockCore.SkyblockCore;
import jefry.plugin.skyblockCore.UI.IslandSelectionUI;
import jefry.plugin.skyblockCore.island.Island;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IslandManager {
    private final SkyblockCore plugin;
    private final Map<UUID, Island> islands = new HashMap<>();
    private final Map<UUID, Boolean> createdIslands = new HashMap<>();
    private final File savesFolder;
    private final FileConfiguration config;
    private final File defaultIslandFile;
    private final Map<UUID, Location> islandLocations = new HashMap<>(); // Stores island locations by player UUID


    public IslandManager(SkyblockCore plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.savesFolder = new File(plugin.getDataFolder(), "saves");
        if (!savesFolder.exists()) savesFolder.mkdirs();

        // Load the default island file from config
        String defaultIslandPath = config.getString("default-island-file");
        if (defaultIslandPath != null) {
            this.defaultIslandFile = new File(savesFolder, defaultIslandPath);
        } else {
            this.defaultIslandFile = null;
        }
    }

    // Create a new island for a player, limited to one creation
    public void createIsland(Player player) {
        UUID playerId = player.getUniqueId();

        // Check if the player has already created an island
        if (createdIslands.getOrDefault(playerId, false)) {
            player.sendMessage("You have already created an island.");
            return;
        }

        // Open the selection UI to choose an island
        IslandSelectionUI.openIslandSelectionUI(player, plugin);

        // The island creation logic will now be handled after player selection in IslandSelectionUI
    }

    // Method to actually create the island after selection
    public void createIslandFromTemplate(Player player, String templateName) {
        UUID playerId = player.getUniqueId();

        // Ensure we're using the "sky" world
        World skyWorld = Bukkit.getWorld("sky");
        if (skyWorld == null) {
            player.sendMessage("The 'sky' world does not exist. Please create it using /island createworld.");
            return;
        }

        // Check if the player already has an island
        if (createdIslands.getOrDefault(playerId, false)) {
            player.sendMessage("You already have an island. Use /island tp to teleport there.");
            return;
        }

        // Find next available location and create the island there
        Location islandLocation = findNextAvailableLocation(skyWorld);

        // Save the island template at the new location
        File islandFile = new File(savesFolder, templateName + ".yml");
        if (islandFile.exists()) {
            importIslandFromFile(player, islandLocation, islandFile);
            player.teleport(islandLocation);

            // Store the player's island location and mark the island as created
            islandLocations.put(playerId, islandLocation); // Save in memory
            createdIslands.put(playerId, true); // Mark as created
            saveIslandLocation(playerId, islandLocation); // Save the location to a file
            player.sendMessage("Your " + templateName + " island has been created in the 'sky' world!");
        } else {
            player.sendMessage("Selected island template not found.");
        }
    }

    private void saveIslandLocation(UUID playerId, Location location) {
        File locationFile = new File(savesFolder, "island_locations.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(locationFile);

        // Save the player's island coordinates
        config.set(playerId + ".world", location.getWorld().getName());
        config.set(playerId + ".x", location.getX());
        config.set(playerId + ".y", location.getY());
        config.set(playerId + ".z", location.getZ());

        try {
            config.save(locationFile);
            plugin.getLogger().info("Island location for player " + playerId + " has been saved.");
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save island location for player " + playerId);
            e.printStackTrace();
        }
    }


    // Import island from a default file for the player, adjusted to import in front of the player
    private void importIslandFromFile(Player player, Location originalLocation, File islandFile) {
        if (!islandFile.exists()) {
            player.sendMessage("The island file does not exist.");
            return;
        }

        // Calculate the player's current location and the location in front of them
        Location playerLocation = player.getLocation();
        Location importLocation = playerLocation.add(playerLocation.getDirection().multiply(10)); // 10 blocks in front

        // Adjust the import based on the difference between the original saved location and the current player location
        Location offset = calculateOffset(originalLocation, importLocation);

        try {
            // Load the island in front of the player based on the offset
            loadRegionFromFile(originalLocation, offset, islandFile);
            player.sendMessage("Island imported successfully in front of you!");
        } catch (Exception e) {
            player.sendMessage("Failed to import the island.");
            e.printStackTrace();
        }
    }

    public void loadIslandLocations() {
        File locationFile = new File(savesFolder, "island_locations.yml");
        if (!locationFile.exists()) {
            plugin.getLogger().warning("No island locations file found.");
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(locationFile);

        // Loop through stored players and load their island locations
        for (String key : config.getKeys(false)) {
            UUID playerId = UUID.fromString(key);
            World world = Bukkit.getWorld(config.getString(key + ".world"));
            double x = config.getDouble(key + ".x");
            double y = config.getDouble(key + ".y");
            double z = config.getDouble(key + ".z");

            Location location = new Location(world, x, y, z);
            islandLocations.put(playerId, location);
        }

        plugin.getLogger().info("Island locations have been loaded.");
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

    private Location findNextAvailableLocation(World world) {
        int gridSize = 500; // Distance between islands
        int index = islands.size(); // Total number of islands created so far

        // Ensure we avoid overwriting positions. Calculate position based on grid coordinates
        int x = (index % 10) * gridSize;  // Change rows every 10 islands
        int z = (index / 10) * gridSize;  // Move to next "row" of islands after every 10

        // Return the new island location in the void world (Y level = 100)
        return new Location(world, x, 100, z); // Y=100 for default island height
    }


    // Load the region from file and apply the offset
    private void loadRegionFromFile(Location originalLocation, Location offset, File file) {
        if (!file.exists()) {
            System.out.println("The file does not exist!");
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        int blockIndex = 0;

        // Loop through all saved blocks and apply them with the offset
        while (config.contains("blocks." + blockIndex)) {
            String blockTypeStr = config.getString("blocks." + blockIndex + ".type");
            int x = config.getInt("blocks." + blockIndex + ".x") + offset.getBlockX();
            int y = config.getInt("blocks." + blockIndex + ".y") + offset.getBlockY();
            int z = config.getInt("blocks." + blockIndex + ".z") + offset.getBlockZ();
            String blockDataStr = config.getString("blocks." + blockIndex + ".data");

            Material blockType = Material.valueOf(blockTypeStr);
            Block block = originalLocation.getWorld().getBlockAt(x, y, z);
            block.setType(blockType);

            // Apply block data (like orientation, etc.)
            block.setBlockData(org.bukkit.Bukkit.createBlockData(blockDataStr));

            blockIndex++;
        }
    }

    // Calculate the offset between original location and player's current import location
    private Location calculateOffset(Location original, Location target) {
        int xOffset = target.getBlockX() - original.getBlockX();
        int yOffset = target.getBlockY() - original.getBlockY();
        int zOffset = target.getBlockZ() - original.getBlockZ();
        return new Location(target.getWorld(), xOffset, yOffset, zOffset);
    }

    // Method to save a single island's data
    public void saveIslandData(UUID playerId, Island island) {
        File islandFile = new File(savesFolder, playerId.toString() + "_island.yml");
        island.save(islandFile);
    }

    public void saveAllIslands() {
        for (Map.Entry<UUID, Island> entry : islands.entrySet()) {
            UUID playerId = entry.getKey();
            Island island = entry.getValue();
            saveIslandData(playerId, island);  // Save each island
        }
        plugin.getLogger().info("All islands have been saved.");
    }

    public void exportIsland(Player player) {
        // Assuming you have a selection system with pos1 and pos2
        if (!plugin.getIslandSelectionManager().isSelectionComplete(player)) {
            player.sendMessage("You need to set both positions using /pos1 and /pos2.");
            return;
        }

        Location pos1 = plugin.getIslandSelectionManager().getPos1(player);
        Location pos2 = plugin.getIslandSelectionManager().getPos2(player);

        File exportFile = new File(savesFolder, player.getUniqueId() + "_island_export.yml");

        try {
            saveRegionToFile(pos1, pos2, exportFile);
            player.sendMessage("Island exported to file!");
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("Failed to export island.");
        }
    }

    public void importIsland(Player player) {
        // Assuming a selection system exists for the positions
        if (!plugin.getIslandSelectionManager().isSelectionComplete(player)) {
            player.sendMessage("You need to set both positions using /pos1 and /pos2.");
            return;
        }

        Location pos1 = plugin.getIslandSelectionManager().getPos1(player);
        Location pos2 = plugin.getIslandSelectionManager().getPos2(player);

        File importFile = new File(savesFolder, player.getUniqueId() + "_island_export.yml");

        if (importFile.exists()) {
            loadRegionFromFile(pos1, pos2, importFile);
            player.sendMessage("Island imported successfully!");
        } else {
            player.sendMessage("Island export file not found.");
        }
    }

    public void upgradeGenerator(Player player) {
        UUID playerId = player.getUniqueId();
        Island island = getIsland(playerId);

        if (island == null) {
            player.sendMessage("You don't have an island to upgrade the generator.");
            return;
        }

        // Assuming Island has a generator level and you upgrade based on resources
        int currentLevel = island.getGeneratorLevel();
        int nextLevel = currentLevel + 1;

        // Placeholder for upgrade conditions (e.g., cost, resources required)
        if (canUpgradeGenerator(player, currentLevel)) {
            island.setGeneratorLevel(nextLevel); // Assuming Island class has setGeneratorLevel
            player.sendMessage("Your generator has been upgraded to level " + nextLevel + "!");
            saveIslandData(playerId, island);  // Save the updated island data
        } else {
            player.sendMessage("You don't have enough resources to upgrade the generator.");
        }
    }

    // Placeholder method to check if player can upgrade the generator
    private boolean canUpgradeGenerator(Player player, int currentLevel) {
        // Add logic to check for in-game currency, items, etc.
        return true; // Placeholder, assuming they can upgrade for now
    }

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
        config.save(file);
    }

    public Location getIslandLocation(UUID playerUUID) {
        // Check if the island location is already stored in memory
        if (islandLocations.containsKey(playerUUID)) {
            return islandLocations.get(playerUUID);
        }

        // If not in memory, load it from the saved island locations file
        File locationFile = new File(savesFolder, "island_locations.yml");
        if (!locationFile.exists()) {
            return null; // No saved locations
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(locationFile);
        String path = playerUUID.toString();

        // Check if there's an entry for this player's island
        if (config.contains(path)) {
            World world = Bukkit.getWorld(config.getString(path + ".world"));
            double x = config.getDouble(path + ".x");
            double y = config.getDouble(path + ".y");
            double z = config.getDouble(path + ".z");
            Location location = new Location(world, x, y, z);
            islandLocations.put(playerUUID, location); // Cache it in memory
            return location;
        }

        return null; // No location found for the player
    }
}
