package jefry.plugin.skyblockCore.Managers;

import jefry.plugin.skyblockCore.SkyblockCore;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.file.YamlConfiguration;

    public class CoinManager {

    private final SkyblockCore plugin;
    private final Map<UUID, Integer> coinBalances = new HashMap<>();
    private final File coinsFolder;

    public CoinManager(SkyblockCore plugin) {
        this.plugin = plugin;
        this.coinsFolder = new File(plugin.getDataFolder(), "coins");
        if (!coinsFolder.exists()) {
            coinsFolder.mkdirs();
        }
    }

    public int getCoins(UUID playerId) {
        if (!coinBalances.containsKey(playerId)) {
            loadCoins(playerId);
        }
        return coinBalances.getOrDefault(playerId, 0);
    }

    public void addCoins(UUID playerId, int amount) {
        int currentCoins = getCoins(playerId);
        coinBalances.put(playerId, currentCoins + amount);
        saveCoins(playerId);
    }

    public boolean deductCoins(UUID playerId, int amount) {
        int currentCoins = getCoins(playerId);
        if (currentCoins >= amount) {
            coinBalances.put(playerId, currentCoins - amount);
            saveCoins(playerId);
            return true;
        }
        return false;
    }

    private void loadCoins(UUID playerId) {
        File coinFile = new File(coinsFolder, playerId.toString() + ".yml");
        if (coinFile.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(coinFile);
            int coins = config.getInt("coins");
            coinBalances.put(playerId, coins);
        } else {
            coinBalances.put(playerId, 0); // Default to 0 coins
        }
    }

    private void saveCoins(UUID playerId) {
        File coinFile = new File(coinsFolder, playerId.toString() + ".yml");
        YamlConfiguration config = new YamlConfiguration();
        config.set("coins", coinBalances.get(playerId));
        try {
            config.save(coinFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
