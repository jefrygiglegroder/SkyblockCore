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

    public int getFarmingLevel() {
        return farmingLevel;
    }

    public void increaseFarmingLevel() {
        this.farmingLevel++;
    }

    public int getMiningLevel() {
        return miningLevel;
    }

    public void increaseMiningLevel() {
        this.miningLevel++;
    }

    public int getSwordsmanshipLevel() {
        return swordsmanshipLevel;
    }

    public void increaseSwordsmanshipLevel() {
        this.swordsmanshipLevel++;
    }

    public int getBowmanshipLevel() {
        return bowmanshipLevel;
    }

    public void increaseBowmanshipLevel() {
        this.bowmanshipLevel++;
    }

    public int getFarmingXP() {
        return farmingXP;
    }

    public void addFarmingXP(int xp) {
        this.farmingXP += xp;
        // Level up if enough XP
        if (this.farmingXP >= getXPRequiredForNextLevel(farmingLevel)) {
            this.farmingXP -= getXPRequiredForNextLevel(farmingLevel);
            increaseFarmingLevel();
        }
    }

    public int getMiningXP() {
        return miningXP;
    }

    public void addMiningXP(int xp) {
        this.miningXP += xp;
        // Level up if enough XP
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
        // Level up if enough XP
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
        // Level up if enough XP
        if (this.bowmanshipXP >= getXPRequiredForNextLevel(bowmanshipLevel)) {
            this.bowmanshipXP -= getXPRequiredForNextLevel(bowmanshipLevel);
            increaseBowmanshipLevel();
        }
    }

    private int getXPRequiredForNextLevel(int currentLevel) {
        // Define XP required for the next level
        return 100 * currentLevel; // Example formula
    }

    // Save stats to a file
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
