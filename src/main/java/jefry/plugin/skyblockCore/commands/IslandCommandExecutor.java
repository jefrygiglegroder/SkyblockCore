package jefry.plugin.skyblockCore.commands;

import jefry.plugin.skyblockCore.SkyblockCore;
import jefry.plugin.skyblockCore.UI.UpgradeUI;
import jefry.plugin.skyblockCore.UI.StatsUI; // Import StatsUI
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class IslandCommandExecutor implements CommandExecutor {

    private final SkyblockCore plugin;

    public IslandCommandExecutor(SkyblockCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("Usage: /island <create|join|upgrade|leave|stats>");
            return true;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "create":
                createIsland(player);
                break;
            case "join":
                joinIsland(player);
                break;
            case "upgrade":
                upgradeIsland(player);
                break;
            case "leave":
                leaveIsland(player);
                break;
            case "stats":
                openStats(player);
                break;
            default:
                player.sendMessage("Unknown subcommand. Usage: /island <create|join|upgrade|leave|stats>");
                break;
        }
        return true;
    }

    private void createIsland(Player player) {
        plugin.getIslandManager().createIsland(player);
        player.sendMessage("Island created!");
    }

    private void joinIsland(Player player) {
        plugin.getIslandManager().loadIsland(player);
        player.sendMessage("You have joined your island!");
    }

    private void upgradeIsland(Player player) {
        UpgradeUI upgradeUI = new UpgradeUI(plugin);
        upgradeUI.openUpgradeMenu(player);
        player.sendMessage("Opening the upgrade menu...");
    }

    private void leaveIsland(Player player) {
        UUID playerId = player.getUniqueId();
        plugin.getIslandManager().saveIsland(playerId);
        player.teleport(player.getWorld().getSpawnLocation());
        player.sendMessage("You have left your island!");
    }

    // New method to open the Stats UI
    private void openStats(Player player) {
        StatsUI statsUI = new StatsUI(plugin);
        statsUI.openStatsMenu(player);
        player.sendMessage("Opening the stats menu...");
    }
}
