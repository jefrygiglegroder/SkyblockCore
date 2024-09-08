package jefry.plugin.skyblockCore.island;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerStats {

    private final UUID playerId;

    // Define various stats and their levels
    private int farmingLevel = 1;
    private int miningLevel = 1;
    private int swordsmanshipLevel = 1;
    private int bowmanshipLevel = 1;

    // Experience points for each stat
    private int farmingXP = 0;
    private int miningXP = 0;
    private int swordsmanshipXP = 0;
    private int bowmanshipXP = 0;

    // Constructor
    public PlayerStats(UUID playerId) {
        this.playerId = playerId;
    }

    // Getters and Leveling Up Methods
    public int getFarmingLevel() {
        return farmingLevel;
    }

    public void increaseFarmingLevel() {
        this.farmingLevel++;
        // Notify the player about leveling up (optional if this is handled elsewhere)
        System.out.println("Farming Level increased to " + farmingLevel + "!");
    }

    public int getMiningLevel() {
        return miningLevel;
    }

    public void increaseMiningLevel() {
        this.miningLevel++;
        System.out.println("Mining Level increased to " + miningLevel + "!");
    }

    public int getSwordsmanshipLevel() {
        return swordsmanshipLevel;
    }

    public void increaseSwordsmanshipLevel() {
        this.swordsmanshipLevel++;
        System.out.println("Swordsmanship Level increased to " + swordsmanshipLevel + "!");
    }

    public int getBowmanshipLevel() {
        return bowmanshipLevel;
    }

    public void increaseBowmanshipLevel() {
        this.bowmanshipLevel++;
        System.out.println("Bowmanship Level increased to " + bowmanshipLevel + "!");
    }

    // XP Getters and XP Adding Methods with Auto-Leveling
    public int getFarmingXP() {
        return farmingXP;
    }

    public void addFarmingXP(int xp) {
        this.farmingXP += xp;
        if (this.farmingXP >= getXPRequiredForNextLevel(farmingLevel)) {
            this.farmingXP -= getXPRequiredForNextLevel(farmingLevel); // Rollover XP
            increaseFarmingLevel();
        }
    }

    public int getMiningXP() {
        return miningXP;
    }

    public void addMiningXP(int xp) {
        this.miningXP += xp;
        if (this.miningXP >= getXPRequiredForNextLevel(miningLevel)) {
            this.miningXP -= getXPRequiredForNextLevel(miningLevel);
            increaseMiningLevel();
        }
    }

    public int getSwordsmanshipXP() {
        return swordsmanshipXP;
    }

    public void addSwordsmanshipXP(int xp) {
        this.swordsmanshipXP += xp;
        if (this.swordsmanshipXP >= getXPRequiredForNextLevel(swordsmanshipLevel)) {
            this.swordsmanshipXP -= getXPRequiredForNextLevel(swordsmanshipLevel);
            increaseSwordsmanshipLevel();
        }
    }

    public int getBowmanshipXP() {
        return bowmanshipXP;
    }

    public void addBowmanshipXP(int xp) {
        this.bowmanshipXP += xp;
        if (this.bowmanshipXP >= getXPRequiredForNextLevel(bowmanshipLevel)) {
            this.bowmanshipXP -= getXPRequiredForNextLevel(bowmanshipLevel);
            increaseBowmanshipLevel();
        }
    }

    // Calculate the XP required for the next level (can be configured or dynamic)
    private int getXPRequiredForNextLevel(int currentLevel) {
        return 100 * currentLevel; // Example formula: increases by 100 XP per level
    }

    // Save stats to a file (asynchronously if needed for performance)
    public void save(File file) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("farmingLevel", farmingLevel);
        config.set("miningLevel", miningLevel);
        config.set("swordsmanshipLevel", swordsmanshipLevel);
        config.set("bowmanshipLevel", bowmanshipLevel);
        config.set("farmingXP", farmingXP);
        config.set("miningXP", miningXP);
        config.set("swordsmanshipXP", swordsmanshipXP);
        config.set("bowmanshipXP", bowmanshipXP);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load stats from a file
    public static PlayerStats load(File file, UUID playerId) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        PlayerStats stats = new PlayerStats(playerId);
        stats.farmingLevel = config.getInt("farmingLevel", 1);
        stats.miningLevel = config.getInt("miningLevel", 1);
        stats.swordsmanshipLevel = config.getInt("swordsmanshipLevel", 1);
        stats.bowmanshipLevel = config.getInt("bowmanshipLevel", 1);
        stats.farmingXP = config.getInt("farmingXP", 0);
        stats.miningXP = config.getInt("miningXP", 0);
        stats.swordsmanshipXP = config.getInt("swordsmanshipXP", 0);
        stats.bowmanshipXP = config.getInt("bowmanshipXP", 0);
        return stats;
    }
}
