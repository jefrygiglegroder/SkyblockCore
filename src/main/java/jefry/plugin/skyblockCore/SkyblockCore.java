package jefry.plugin.skyblockCore;

import jefry.plugin.skyblockCore.Listeners.PlayerEventListener;
import jefry.plugin.skyblockCore.Managers.IslandManager;
import jefry.plugin.skyblockCore.UI.StatsUI;
import jefry.plugin.skyblockCore.UI.UpgradeUI;
import jefry.plugin.skyblockCore.commands.IslandCommandExecutor;
import jefry.plugin.skyblockCore.commands.IslandTabCompleter;
import jefry.plugin.skyblockCore.island.CobblestoneGenerator;
import jefry.plugin.skyblockCore.island.PlayerStats;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkyblockCore extends JavaPlugin {

    private IslandManager islandManager;
    private StatsUI statsUI;

    // Map to store player stats
    private final Map<UUID, PlayerStats> playerStatsMap = new HashMap<UUID, PlayerStats>();

    @Override
    public void onEnable() {
        // Initialize island manager
        islandManager = new IslandManager(this);

        // Initialize and register stats UI
        statsUI = new StatsUI(this);
        getServer().getPluginManager().registerEvents(statsUI, this);

        // Register upgrade UI
        UpgradeUI upgradeUI = new UpgradeUI(this);
        getServer().getPluginManager().registerEvents(upgradeUI, this);

        // Register events and commands
        getServer().getPluginManager().registerEvents(new PlayerEventListener(this), this);
        getServer().getPluginManager().registerEvents(new CobblestoneGenerator(this), this);

        IslandCommandExecutor islandCommandExecutor = new IslandCommandExecutor(this);
        getCommand("island").setExecutor(islandCommandExecutor);
        getCommand("island").setTabCompleter(new IslandTabCompleter());

        getLogger().info("Skyblock Plugin enabled!");
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
}
