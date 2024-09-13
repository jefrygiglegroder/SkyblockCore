package jefry.plugin.skyblockCore;

import jefry.plugin.skyblockCore.Listeners.BlockBreakListener;
import jefry.plugin.skyblockCore.Listeners.EntityKillListener;
import jefry.plugin.skyblockCore.Listeners.PlayerEventListener;
import jefry.plugin.skyblockCore.Managers.CoinManager;
import jefry.plugin.skyblockCore.Managers.IslandManager;
import jefry.plugin.skyblockCore.Managers.IslandSelectionManager;
import jefry.plugin.skyblockCore.UI.StatsUI;
import jefry.plugin.skyblockCore.UI.UpgradeUI;
import jefry.plugin.skyblockCore.commands.IslandCommandExecutor;
import jefry.plugin.skyblockCore.commands.IslandTabCompleter;
import jefry.plugin.skyblockCore.enchantments.AutoPickupEnchantment;
import jefry.plugin.skyblockCore.enchantments.CustomEnchantments;
import jefry.plugin.skyblockCore.island.CobblestoneGenerator;
import jefry.plugin.skyblockCore.island.PlayerStats;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkyblockCore extends JavaPlugin {

    private IslandManager islandManager;
    private CoinManager coinManager; // Added CoinManager

    private IslandSelectionManager islandSelectionManager; // Added IslandSelectionManager

    private StatsUI statsUI;

    public static Plugin plugin;

    // Map to store player stats
    private final Map<UUID, PlayerStats> playerStatsMap = new HashMap<UUID, PlayerStats>();

    @Override
    public void onEnable() {
        try {
            // Register custom enchantments
            CustomEnchantments.registerAllEnchantments();
            getLogger().info("Custom enchantments registered successfully.");
        } catch (Exception e) {
            getLogger().severe("Failed to register custom enchantments: " + e.getMessage());
        }

        plugin = this;

        // Initialize island manager
        islandManager = new IslandManager(this);
        coinManager = new CoinManager(this);

        // Initialize and register stats UI
        statsUI = new StatsUI(this);
        getServer().getPluginManager().registerEvents(statsUI, this);

        // Register upgrade UI
        UpgradeUI upgradeUI = new UpgradeUI(this);
        getServer().getPluginManager().registerEvents(upgradeUI, this);

        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityKillListener(this), this);

        // Register events and commands
        getServer().getPluginManager().registerEvents(new PlayerEventListener(this), this);
        getServer().getPluginManager().registerEvents(new CobblestoneGenerator(this), this);

        IslandCommandExecutor islandCommandExecutor = new IslandCommandExecutor(this);
        getCommand("island").setExecutor(new IslandCommandExecutor(this));
        getCommand("island").setTabCompleter(new IslandTabCompleter());

        coinManager = new CoinManager(this);  // Initialize CoinManager
        islandSelectionManager = new IslandSelectionManager();  // Initialize selection manager

        getLogger().info("Skyblock Plugin enabled!");
        // Check if the world exists
        World skyblockWorld = getServer().getWorld("sky");
        if (skyblockWorld == null) {
            getLogger().info("Skyblock world does not exist, creating world...");
            WorldCreator worldCreator = new WorldCreator("sky");
            worldCreator.generator(new VoidWorldGenerator());
            skyblockWorld = worldCreator.createWorld();
        } else {
            getLogger().info("Skyblock world already exists, loading world...");
        }
    }

    @Override
    public void onDisable() {
        islandManager.saveAllIslands();
        getLogger().info("Skyblock Plugin disabled!");
    }

    public IslandManager getIslandManager() {
        return islandManager;
    }
    public PlayerStats getPlayerStats(UUID playerId) {
        if (!playerStatsMap.containsKey(playerId)) {
            File statsFile = new File(getDataFolder(), "saves/" + playerId.toString() + ".yml");
            if (statsFile.exists()) {
                PlayerStats stats = PlayerStats.load(statsFile, playerId);
                playerStatsMap.put(playerId, stats);
            } else {
                PlayerStats stats = new PlayerStats(playerId); // default stats
                playerStatsMap.put(playerId, stats);
            }
        }
        return playerStatsMap.get(playerId);
    }

    public void savePlayerStats(UUID playerId) {
        PlayerStats stats = playerStatsMap.get(playerId);
        if (stats != null) {
            File statsFile = new File(getDataFolder(), "saves/" + playerId.toString() + ".yml");
            stats.save(statsFile);
        }
    }

    public void saveAllPlayerStats() {
        for (UUID playerId : playerStatsMap.keySet()) {
            savePlayerStats(playerId);
        }
    }
    public CoinManager getCoinManager() {
        return this.coinManager;
    }
    public IslandSelectionManager getIslandSelectionManager() {
        return islandSelectionManager;
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        World loadedWorld = event.getWorld();
        if (loadedWorld.getName().equals("sky")) {
            getLogger().info("Skyblock world has been loaded.");
            // Initialize anything else required after the world has been loaded
        }
    }

}
